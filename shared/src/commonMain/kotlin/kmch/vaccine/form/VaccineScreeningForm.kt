package kmch.vaccine.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun VaccineScreeningForm() {
    val apiService = remember { VaccineApiService() }
    val coroutineScope = rememberCoroutineScope()

    // ---- ข้อมูลผู้ป่วย (จำเป็นสำหรับ POST /api/v1/registrations) ----
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
        // kotlinx.datetime.Clock.System fails to link on the Wasm target (IrLinkageError);
        // kotlin.time.Instant's ISO-8601 string always starts with "yyyy-MM-dd".
        mutableStateOf(Clock.System.now().toString().substring(0, 10))
    }

    var submitError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // ---- คำถามคัดกรอง ----
    var questionList by remember { mutableStateOf<List<ScreeningQuestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var isSubmitted by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var acceptedVaccineInfo by remember { mutableStateOf(false) }

    val answers = remember { mutableStateMapOf<Int, Boolean>() }
    val remarks = remember { mutableStateMapOf<Int, String>() }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        try {
            questionList = apiService.getScreeningQuestions().sortedBy { it.displayOrder }
        } catch (e: Exception) {
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
        DocumentType.ID_CARD -> idCard.isNotBlank()
        DocumentType.PASSPORT -> passportId.isNotBlank()
    }
    val isPatientInfoValid = prefix.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() &&
        sex != null && patientType != null && isDocumentValid &&
        parsedAge != null && parsedAge > 0 && telNo.isNotBlank() && parsedShotDate != null

    if (!isSubmitted) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "แบบสอบถามผู้มารับบริการ วัคซีนไข้หวัดใหญ่ตามฤดูกาล",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "กรุณาทำเครื่องหมายตามความเป็นจริงเพื่อเจ้าหน้าที่จะได้พิจารณาว่าท่านสามารถฉีดวัคซีนได้หรือไม่",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))

                // ==================== ข้อมูลผู้มารับบริการ ====================
                Text("ข้อมูลผู้มารับบริการ", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = prefix,
                        onValueChange = { prefix = it },
                        label = { Text("คำนำหน้า") },
                        placeholder = { Text("นาย / นาง / นางสาว") },
                        modifier = Modifier.width(160.dp)
                    )
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("ชื่อ") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("นามสกุล") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text("เพศ", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = sex == Sex.MALE, onClick = { sex = Sex.MALE })
                    Text("ชาย")
                    Spacer(modifier = Modifier.width(24.dp))
                    RadioButton(selected = sex == Sex.FEMALE, onClick = { sex = Sex.FEMALE })
                    Text("หญิง")
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text("ประเภทผู้มารับบริการ", style = MaterialTheme.typography.bodyMedium)
                Column {
                    val patientTypeOptions = listOf(
                        PatientType.STUDENT to "นักศึกษา",
                        PatientType.EMPLOYEE to "พนักงาน",
                        PatientType.COMPANY to "บริษัทคู่สัญญา",
                        PatientType.EXTERNAL to "บุคคลภายนอก"
                    )
                    patientTypeOptions.forEach { (type, label) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = patientType == type, onClick = { patientType = type })
                            Text(label)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text("เอกสารยืนยันตัวตน", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = documentType == DocumentType.ID_CARD,
                        onClick = { documentType = DocumentType.ID_CARD }
                    )
                    Text("บัตรประชาชน")
                    Spacer(modifier = Modifier.width(24.dp))
                    RadioButton(
                        selected = documentType == DocumentType.PASSPORT,
                        onClick = { documentType = DocumentType.PASSPORT }
                    )
                    Text("พาสปอร์ต")
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (documentType == DocumentType.ID_CARD) {
                    OutlinedTextField(
                        value = idCard,
                        onValueChange = { idCard = it },
                        label = { Text("เลขบัตรประจำตัวประชาชน") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = passportId,
                        onValueChange = { passportId = it },
                        label = { Text("เลขที่พาสปอร์ต") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { new -> age = new.filter { it.isDigit() } },
                        label = { Text("อายุ") },
                        modifier = Modifier.width(120.dp)
                    )
                    OutlinedTextField(
                        value = telNo,
                        onValueChange = { telNo = it },
                        label = { Text("เบอร์โทรศัพท์") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = shotDate,
                        onValueChange = { shotDate = it },
                        label = { Text("วันที่รับวัคซีน (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = underlyingDisease,
                    onValueChange = { underlyingDisease = it },
                    label = { Text("โรคประจำตัว (ถ้ามี)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("ที่อยู่ (ถ้ามี)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = zipCode,
                        onValueChange = { new -> zipCode = new.filter { it.isDigit() } },
                        label = { Text("รหัสไปรษณีย์") },
                        modifier = Modifier.width(140.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // ==================== คำถามคัดกรอง ====================
                Text("แบบคัดกรองสุขภาพ", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (questionList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ไม่พบข้อมูลแบบคัดกรองในขณะนี้ กรุณาติดต่อเจ้าหน้าที่",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
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
                                        label = { Text("ระบุคำตอบของคุณ") },
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
                                        Text("มี / ใช่")
                                        Spacer(modifier = Modifier.width(24.dp))

                                        RadioButton(
                                            selected = answers[qId] == false,
                                            onClick = {
                                                answers[qId] = false
                                                remarks.remove(qId)
                                            }
                                        )
                                        Text("ไม่มี / ไม่ใช่")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

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
                        append("หากมีข้อสงสัยเกี่ยวกับการคุ้มครองข้อมูลส่วนบุคคล สามารถสืบค้นข้อมูลเพิ่มเติมและช่องทางติดต่อได้ที่เว็บไซต์ ")

                        pushStringAnnotation(
                            tag = "URL",
                            annotation = "https://pdpa.kmitl.ac.th"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("pdpa.kmitl.ac.th")
                        }
                        pop()
                    }

                    ClickableText(
                        text = pdpaAnnotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(top = 12.dp),
                        onClick = { offset ->
                            pdpaAnnotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            )
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
                        text = "ท่านได้รับข้อมูลเกี่ยวกับวัคซีนไข้หวัดใหญ่และได้ทำความเข้าใจแล้ว",
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

                                // isPatientInfoValid (ซึ่งคุม enabled ของปุ่มนี้) การันตีแล้วว่าค่าเหล่านี้ไม่ null
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

                                apiService.submitRegistration(requestData)
                                isSubmitted = true
                            } catch (e: ApiException) {
                                submitError = when (e.statusCode) {
                                    400 -> "ข้อมูลไม่ถูกต้องหรือเอกสารซ้ำในระบบ กรุณาตรวจสอบอีกครั้ง"
                                    409 -> "วัคซีนหมดสต็อกในขณะนี้ กรุณาติดต่อเจ้าหน้าที่"
                                    else -> "เกิดข้อผิดพลาด (${e.statusCode}) กรุณาลองใหม่อีกครั้ง"
                                }
                            } catch (e: Exception) {
                                submitError = "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้ กรุณาลองใหม่อีกครั้ง"
                                println("Network/Exception Error submitting form: ${e.message}")
                            } finally {
                                isSubmitting = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = acceptedTerms && acceptedVaccineInfo && isAllQuestionsAnswered && isPatientInfoValid && !isSubmitting
                ) {
                    Text(if (isSubmitting) "กำลังบันทึก..." else "ยืนยันบันทึกข้อมูล")
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "บันทึกข้อมูลสำเร็จ! ท่านสามารถออกจากหน้านี้ได้",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
