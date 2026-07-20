package kmch.vaccine.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineScreeningForm() {
    val apiService = remember { VaccineApiService() }
    val coroutineScope = rememberCoroutineScope()

    // ---- ข้อมูลผู้ป่วย ----
    var prefix by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf<Sex?>(null) }
    var patientType by remember { mutableStateOf<PatientType?>(null) }
    var documentType by remember { mutableStateOf(DocumentType.ID_CARD) }
    var idCard by remember { mutableStateOf("") }
    var passportId by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var telNo by remember { mutableStateOf("") }
    var underlyingDisease by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var shotDate by remember {
        mutableStateOf(Clock.System.now().toString().substring(0, 10))
    }

    var submitError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // ---- Dropdown States ----
    var expandedPrefix by remember { mutableStateOf(false) }
    val prefixOptions = listOf("นาย/MR.", "นาง/MS.", "นางสาว/MRS.")

    var expandedPatientType by remember { mutableStateOf(false) }
    val patientTypeOptions = mapOf(
        PatientType.STUDENT to "นักศึกษา สจล./KMITL Student",
        PatientType.EMPLOYEE to "พนักงาน/KMITL Employee",
        PatientType.COMPANY to "บริษัทคู่สัญญา/Contracted Company",
        PatientType.EXTERNAL to "บุคคลภายนอก/External"
    )

    // ---- DatePicker State ----
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = remember {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val todayStartMillis = (Clock.System.now().toEpochMilliseconds() / 86_400_000L) * 86_400_000L
                    return utcTimeMillis >= todayStartMillis
                }
            }
        }
    )

    // ---- คำถามคัดกรอง ----
    var questionList by remember { mutableStateOf<List<ScreeningQuestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var retryTrigger by remember { mutableStateOf(0) }

    var isSubmitted by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var acceptedVaccineInfo by remember { mutableStateOf(false) }

    var successResponse by remember { mutableStateOf<RegistrationResponse?>(null) }

    val answers = remember { mutableStateMapOf<Int, Boolean>() }
    val remarks = remember { mutableStateMapOf<Int, String>() }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(retryTrigger) {
        isLoading = true
        loadError = null
        try {
            questionList = apiService.getScreeningQuestions().sortedBy { it.displayOrder }
        } catch (e: Exception) {
            loadError = "โหลดแบบคัดกรองไม่สำเร็จ กรุณาตรวจสอบการเชื่อมต่อแล้วลองใหม่อีกครั้ง\nFailed to load the screening form. Please check your connection and try again."
            println("Error fetching questions: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    val isAllQuestionsAnswered = questionList.all { question ->
        val isTextQuestion = question.questionText.startsWith("[Text]")
        if (isTextQuestion) {
            !remarks[question.questionId].isNullOrBlank()
        } else {
            answers.containsKey(question.questionId)
        }
    }

    val parsedAge = age.toIntOrNull()
    val parsedShotDate = runCatching { LocalDate.parse(shotDate) }.getOrNull()

    val isDocumentValid = when (documentType) {
        DocumentType.ID_CARD -> idCard.length == 13
        DocumentType.PASSPORT -> passportId.isNotBlank()
    }
    val isPhoneValid = telNo.length == 10
    val isPatientInfoValid = prefix.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() &&
            sex != null && patientType != null && isDocumentValid &&
            parsedAge != null && parsedAge > 0 && isPhoneValid && address.isNotBlank() && parsedShotDate != null

    if (!isSubmitted) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (loadError != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = loadError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { retryTrigger++ }) {
                    Text("ลองใหม่อีกครั้ง / Try Again")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "แบบสอบถามผู้มารับบริการ วัคซีนไข้หวัดใหญ่ตามฤดูกาล\nSeasonal Influenza Vaccine Screening Form",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "กรุณาทำเครื่องหมายตามความเป็นจริงเพื่อเจ้าหน้าที่จะได้พิจารณาว่าท่านสามารถฉีดวัคซีนได้หรือไม่\nPlease mark truthfully so the staff can determine if you are eligible to receive the vaccine.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))

                // ==================== ข้อมูลผู้มารับบริการ ====================
                Text("ข้อมูลผู้มารับบริการ / Patient Information", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                // คำนำหน้า (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expandedPrefix,
                    onExpandedChange = { expandedPrefix = !expandedPrefix }
                ) {
                    OutlinedTextField(
                        value = prefix,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("คำนำหน้า / Prefix") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrefix) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPrefix,
                        onDismissRequest = { expandedPrefix = false }
                    ) {
                        prefixOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    prefix = option
                                    expandedPrefix = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // ชื่อ - นามสกุล
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("ชื่อ / First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("นามสกุล / Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // เพศ
                Text("เพศ / Sex", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { sex = Sex.MALE },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (sex == Sex.MALE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (sex == Sex.MALE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) { Text("ชาย / Male") }

                    Button(
                        onClick = { sex = Sex.FEMALE },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (sex == Sex.FEMALE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (sex == Sex.FEMALE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) { Text("หญิง / Female") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // ประเภทผู้มารับบริการ
                Text("ประเภทผู้มารับบริการ / Patient Type", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedPatientType,
                    onExpandedChange = { expandedPatientType = !expandedPatientType }
                ) {
                    OutlinedTextField(
                        value = patientTypeOptions[patientType] ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("เลือกประเภทผู้รับบริการ / Select Patient Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPatientType) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPatientType,
                        onDismissRequest = { expandedPatientType = false }
                    ) {
                        patientTypeOptions.forEach { (type, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    patientType = type
                                    expandedPatientType = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // เอกสารยืนยันตัวตน
                Text("เอกสารยืนยันตัวตน / Identification Document", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { documentType = DocumentType.ID_CARD },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (documentType == DocumentType.ID_CARD) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (documentType == DocumentType.ID_CARD) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) { Text("บัตรประชาชน / ID Card") }

                    Button(
                        onClick = { documentType = DocumentType.PASSPORT },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (documentType == DocumentType.PASSPORT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (documentType == DocumentType.PASSPORT) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) { Text("พาสปอร์ต / Passport") }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (documentType == DocumentType.ID_CARD) {
                    OutlinedTextField(
                        value = idCard,
                        onValueChange = { new -> idCard = new.filter { it.isDigit() }.take(13) },
                        label = { Text("เลขบัตรประจำตัวประชาชน / ID Card Number") },
                        isError = idCard.isNotEmpty() && idCard.length != 13,
                        supportingText = {
                            if (idCard.isNotEmpty() && idCard.length != 13) {
                                Text("เลขบัตรประชาชนต้องมี 13 หลัก (ตอนนี้ ${idCard.length} หลัก) / Must be 13 digits (Current: ${idCard.length})")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = passportId,
                        onValueChange = { passportId = it },
                        label = { Text("เลขที่พาสปอร์ต / Passport Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // อายุ และ เบอร์โทร
                OutlinedTextField(
                    value = age,
                    onValueChange = { new -> age = new.filter { it.isDigit() } },
                    label = { Text("อายุ / Age") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = telNo,
                    onValueChange = { new -> telNo = new.filter { it.isDigit() }.take(10) },
                    label = { Text("เบอร์โทรศัพท์ / Phone Number") },
                    isError = telNo.isNotEmpty() && telNo.length != 10,
                    supportingText = {
                        if (telNo.isNotEmpty() && telNo.length != 10) {
                            Text("เบอร์โทรศัพท์ต้องมี 10 หลัก (ตอนนี้ ${telNo.length} หลัก) / Must be 10 digits (Current: ${telNo.length})")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // วันที่รับวัคซีน
                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val days = (millis / 86400000).toInt()
                                    val date = LocalDate.fromEpochDays(days)
                                    shotDate = date.toString()
                                }
                                showDatePicker = false
                            }) { Text("ตกลง / OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) { Text("ยกเลิก / Cancel") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                    OutlinedTextField(
                        value = shotDate,
                        onValueChange = {},
                        label = { Text("วันที่รับวัคซีน / Vaccination Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "เลือกวันที่ / Select Date")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // โรคประจำตัว ที่อยู่ รหัสไปรษณีย์
                OutlinedTextField(
                    value = underlyingDisease,
                    onValueChange = { underlyingDisease = it },
                    label = { Text("โรคประจำตัว (ถ้ามี) / Underlying Disease (If any)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("ที่อยู่ / Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { new -> zipCode = new.filter { it.isDigit() } },
                    label = { Text("รหัสไปรษณีย์ / Zip Code") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // ==================== คำถามคัดกรอง ====================
                Text("แบบคัดกรองสุขภาพ / Health Screening", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (questionList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ไม่พบข้อมูลแบบคัดกรองในขณะนี้ กรุณาติดต่อเจ้าหน้าที่\nNo screening questions found at this time. Please contact staff.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    questionList.forEach { question ->
                        val qId = question.questionId
                        val rawText = question.questionText
                        val isTextQuestion = rawText.startsWith("[Text]")
                        val displayText = if (isTextQuestion) rawText.removePrefix("[Text]").trim() else rawText

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(displayText, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(8.dp))

                                if (isTextQuestion) {
                                    OutlinedTextField(
                                        value = remarks[qId] ?: "",
                                        onValueChange = { newValue -> remarks[qId] = newValue },
                                        label = { Text("ระบุคำตอบของคุณ / Specify your answer") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = false,
                                        maxLines = 3
                                    )
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = answers[qId] == true,
                                            onClick = { answers[qId] = true }
                                        )
                                        Text("มี / ใช่ (Yes)")
                                        Spacer(modifier = Modifier.width(24.dp))

                                        RadioButton(
                                            selected = answers[qId] == false,
                                            onClick = {
                                                answers[qId] = false
                                                remarks.remove(qId)
                                            }
                                        )
                                        Text("ไม่มี / ไม่ใช่ (No)")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // PDPA
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    val pdpaAnnotatedString = buildAnnotatedString {
                        append("โรงพยาบาลพระจอมเกล้าเจ้าคุณทหาร สถาบันเทคโนโลยีพระจอมเกล้าเจ้าคุณทหารลาดกระบัง (KMITL) มีการเก็บรวบรวม ใช้ และเปิดเผยข้อมูลส่วนบุคคลของท่าน ประกอบด้วย ชื่อ นามสกุล อีเมล เลขบัตรประจำตัวประชาชน อายุ หมายเลขโทรศัพท์ คณะ ข้อมูลสุขภาพ พฤติกรรม และสุขภาวะแวดล้อม เพื่อดำเนินการ ประสานงาน รวมถึงติดต่อกรณีที่มีอันจำเป็นภายหลังการตรวจสุขภาพ ในโครงการตรวจสุขภาพนักศึกษา ประจำปีงบประมาณ 2569 โดยใช้ “ฐานสัญญาและฐานความจำเป็นเพื่อประโยชน์โดยชอบด้วยกฎหมาย”\n\n")
                        append("ทางโรงพยาบาลยืนยันว่าข้อมูลส่วนบุคคลของท่านจะถูกเก็บเป็นความลับ และไม่มีการเปิดเผยโดยปราศจากความยินยอมของท่าน เว้นแต่เป็นการเปิดเผยตามที่กฎหมายกำหนด หรือตามหน้าที่ หรือเมื่อมีข้อบ่งชี้และความจำเป็นในการวินิจฉัย รักษาโรคและฟื้นฟูสภาพของข้าพเจ้า ซึ่งกรณีที่ตรวจพบความผิดปกติ ทางโรงพยาบาลจะมีการแจ้งผลให้ท่านทราบ เพื่อเป็นการติดตามและดูแลต่อไป\n\n")

                        // English translation for PDPA
                        append("KMITL Hospital collects, uses, and discloses your personal and health data for the purpose of the health screening program. The hospital confirms your personal data will be kept confidential and will not be disclosed without your consent unless required by law or necessary for medical diagnosis and treatment.\n\n")

                        append("หากมีข้อสงสัยเกี่ยวกับการคุ้มครองข้อมูลส่วนบุคคล สามารถสืบค้นข้อมูลเพิ่มเติมและช่องทางติดต่อได้ที่เว็บไซต์ (For further inquiries on PDPA, please visit) ")
                        pushStringAnnotation(tag = "URL", annotation = "https://pdpa.kmitl.ac.th")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) { append("pdpa.kmitl.ac.th") }
                        pop()
                    }

                    ClickableText(
                        text = pdpaAnnotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(top = 12.dp),
                        onClick = { offset ->
                            pdpaAnnotatedString.getStringAnnotations("URL", offset, offset)
                                .firstOrNull()?.let { annotation ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = acceptedVaccineInfo,
                        onCheckedChange = { acceptedVaccineInfo = it },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "ท่านได้รับข้อมูลเกี่ยวกับวัคซีนไข้หวัดใหญ่และได้ทำความเข้าใจแล้ว\nI have received and understood the information regarding the influenza vaccine.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                submitError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            submitError = null
                            isSubmitting = true
                            try {
                                val answerList = questionList.map { question ->
                                    val qId = question.questionId
                                    val isTextQuestion = question.questionText.startsWith("[Text]")
                                    RegistrationAnswerRequest(
                                        questionId = qId,
                                        answer = if (isTextQuestion) false else (answers[qId] == true),
                                        remark = remarks[qId]
                                    )
                                }

                                val requestData = RegistrationRequest(
                                    prefix = prefix,
                                    firstName = firstName,
                                    lastName = lastName,
                                    sex = sex!!,
                                    patientType = patientType!!,
                                    documentType = documentType,
                                    idCard = idCard.ifBlank { null },
                                    passportId = passportId.ifBlank { null },
                                    age = parsedAge!!,
                                    telNo = telNo,
                                    underlyingDisease = underlyingDisease.ifBlank { null },
                                    address = address.ifBlank { null },
                                    zipCode = zipCode.ifBlank { null },
                                    shotDate = parsedShotDate!!,
                                    answers = answerList
                                )

                                val response = apiService.submitRegistration(requestData)
                                successResponse = response
                                isSubmitted = true

                            } catch (e: ApiException) {
                                submitError = when (e.statusCode) {
                                    400 -> "ข้อมูลไม่ถูกต้องหรือเอกสารซ้ำในระบบ กรุณาตรวจสอบอีกครั้ง\nInvalid data or duplicate document in the system. Please check again."
                                    409 -> "วัคซีนหมดสต็อกในขณะนี้ กรุณาติดต่อเจ้าหน้าที่\nVaccine is out of stock. Please contact staff."
                                    else -> "เกิดข้อผิดพลาด (${e.statusCode}) กรุณาลองใหม่อีกครั้ง\nAn error occurred (${e.statusCode}). Please try again."
                                }
                            } catch (e: Exception) {
                                submitError = "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้ กรุณาลองใหม่อีกครั้ง\nCannot connect to the server. Please try again."
                                println("Network/Exception Error submitting form: ${e.message}")
                            } finally {
                                isSubmitting = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = acceptedTerms && acceptedVaccineInfo && isAllQuestionsAnswered && isPatientInfoValid && !isSubmitting
                ) {
                    Text(if (isSubmitting) "กำลังบันทึก... / Submitting..." else "ยืนยันบันทึกข้อมูล / Confirm and Submit")
                }
            }
        }
    } else {
        // ==================== หน้าแสดงผลเมื่อสำเร็จ ====================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "บันทึกข้อมูลสำเร็จ!\nSubmission Successful!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            successResponse?.let { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("รหัสผู้ป่วย (Patient ID): ${res.patientId}", style = MaterialTheme.typography.bodyLarge)
                        Text("รหัสการฉีดวัคซีน (Vaccination ID): ${res.vaccinationId}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "ท่านสามารถแคปหน้าจอนี้ไว้เป็นหลักฐาน\nและออกจากหน้านี้ได้เลย\n\nYou may capture this screen as proof and exit this page.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                prefix = ""
                firstName = ""
                lastName = ""
                sex = null
                patientType = null
                documentType = DocumentType.ID_CARD
                idCard = ""
                passportId = ""
                age = ""
                telNo = ""
                underlyingDisease = ""
                address = ""
                zipCode = ""
                shotDate = Clock.System.now().toString().substring(0, 10)
                answers.clear()
                remarks.clear()
                acceptedTerms = false
                acceptedVaccineInfo = false
                submitError = null
                successResponse = null
                isSubmitted = false
                coroutineScope.launch { scrollState.scrollTo(0) }
            }) {
                Text("กลับสู่หน้าหลัก / Return to Home")
            }
        }
    }
}