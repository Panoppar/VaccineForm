package kmch.vaccine.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.util.PrintAnswerItem
import kmch.vaccine.form.util.printVaccineDocument

@Composable
fun AdminDashBoardScreen(onNavigateBack: () -> Unit) {
    var selectedPatientId by remember { mutableStateOf<Int?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }

    // 1. STATE HOISTING: ย้ายตัวแปรสำหรับเก็บ Lot และ เลขอัตราเจ้าหน้าที่ มาไว้ระดับบนสุด
    // เพื่อให้ข้อมูลไม่หายไปเมื่อมีการสลับ Composable ระหว่าง List และ Detail
    var lotInput by remember { mutableStateOf("") }
    var employeeRateId by remember { mutableStateOf("") }

    // ควบคุมการแสดงผลว่าจะโชว์หน้ารายการ หรือหน้ารายละเอียด
    if (selectedPatientId != null || isCreatingNew) {
        AdminDetailView(
            patientId = selectedPatientId,
            isNew = isCreatingNew,
            // 2. ส่งค่าที่เก็บไว้ลงไปให้ AdminDetailView เพื่อเอาไปใช้ยิง API
            lotInput = lotInput,
            employeeRateId = employeeRateId,
            onBack = {
                selectedPatientId = null
                isCreatingNew = false
            }
        )
    } else {
        AdminListView(
            // 3. ส่ง State และ Lambda function สำหรับอัปเดตค่า ลงไปให้ AdminListView
            lotInput = lotInput,
            onLotInputChange = { lotInput = it },
            employeeRateId = employeeRateId,
            onEmployeeRateIdChange = { employeeRateId = it },

            onPatientSelected = { selectedPatientId = it },
            onCreateNew = { isCreatingNew = true }
        )
    }
}

@Composable
fun AdminListView(
    lotInput: String,
    onLotInputChange: (String) -> Unit,
    employeeRateId: String,
    onEmployeeRateIdChange: (String) -> Unit,
    onPatientSelected: (Int) -> Unit,
    onCreateNew: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // 1. เพิ่ม State สำหรับเก็บข้อมูลจริงจาก API
    val apiService = remember { VaccineApiService() }
    var patientsList by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 2. ดึงข้อมูลเมื่อโหลดหน้านี้ครั้งแรก
    LaunchedEffect(Unit) {
        try {
            patientsList = apiService.getPatients()
        } catch (e: Exception) {
            println("Error fetching patients: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // 3. เปลี่ยนจาก mockPatients เป็น patientsList
    val filteredPatients = remember(searchQuery, patientsList) {
        if (searchQuery.isBlank()) {
            patientsList
        } else {
            patientsList.filter { patient ->
                val idMatch = patient.patientId?.toString()?.contains(searchQuery, ignoreCase = true) == true
                val firstNameMatch = patient.firstName.contains(searchQuery, ignoreCase = true)
                val lastNameMatch = patient.lastName.contains(searchQuery, ignoreCase = true)
                idMatch || firstNameMatch || lastNameMatch
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("แดชบอร์ดจัดการคัดกรองวัคซีน", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // ส่วน Header ควบคุมและค้นหา
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = lotInput, // ใช้ค่าที่รับมาจาก Parameter
                    onValueChange = onLotInputChange, // เรียกใช้งาน Lambda เมื่อมีการพิมพ์
                    label = { Text("Lot วัคซีน") },
                    modifier = Modifier.width(200.dp)
                )
                OutlinedTextField(
                    value = employeeRateId, // ใช้ค่าที่รับมาจาก Parameter
                    onValueChange = onEmployeeRateIdChange, // เรียกใช้งาน Lambda เมื่อมีการพิมพ์
                    label = { Text("เลขอัตราเจ้าหน้าที่") },
                    modifier = Modifier.width(200.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("ค้นหาเลขอัตรา") }
                )
                FloatingActionButton(onClick = onCreateNew) {
                    Text("+")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (filteredPatients.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ไม่พบข้อมูลผู้รับบริการที่ตรงกับ '$searchQuery'",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPatients) { patient ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            patient.patientId?.let { id ->
                                onPatientSelected(id)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("รหัส: ${patient.patientId}", style = MaterialTheme.typography.bodyLarge)
                            Text("ชื่อ-นามสกุล: ${patient.firstName} ${patient.lastName}", style = MaterialTheme.typography.bodyLarge)
                            Text("โรคประจำตัว: ${patient.underlyingDisease}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDetailView(
    patientId: Int?,
    isNew: Boolean,
    lotInput: String,
    employeeRateId: String,
    onBack: () -> Unit
) {
    val apiService = remember { VaccineApiService() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = onBack) {
            Text("< กลับหน้ารายการคัดกรอง")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isNew) {
            Text("กรอกแบบคัดกรองใหม่โดยเจ้าหน้าที่", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            VaccineScreeningForm()

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // TODO (API): POST บันทึกข้อมูลแบบสอบถามเข้าฐานข้อมูล
                        // เตรียมข้อมูลที่จะส่งไป:
                        // val requestData = mapOf(
                        //     "patientId" to "NEW",
                        //     "staffId" to employeeRateId, // นำมาใช้งานตรงนี้ได้เลย
                        //     "lot" to lotInput            // นำมาใช้งานตรงนี้ได้เลย
                        // )
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text("บันทึกและพิมพ์")
                }

                Button(
                    onClick = {
                        // TODO (API): POST ข้อมูลเข้า Vaccination_Record เพื่อตัด Lot วัคซีน
                        // val recordData = mapOf(
                        //     "lotNumber" to lotInput,
                        //     "administeredBy" to employeeRateId
                        // )
                        println("ยืนยันฉีดวัคซีน: Lot=$lotInput, Staff=$employeeRateId")
                    }
                ) {
                    Text("ยืนยันได้รับวัคซีนจริง")
                }
            }
        } else { // กรณีดูรายละเอียดผู้ป่วย (isNew == false)
            // สร้าง State สำหรับเก็บข้อมูล Detail
            var patient by remember { mutableStateOf<Patient?>(null) }
            var latestRecord by remember { mutableStateOf<ScreeningRecord?>(null) }
            var recordAnswers by remember { mutableStateOf<List<ScreeningAnswer>>(emptyList()) }
            var activeQuestions by remember { mutableStateOf<List<ScreeningQuestion>>(emptyList()) }
            var isLoading by remember { mutableStateOf(true) }

            // ยิง API ดึงข้อมูลทั้งหมดที่เกี่ยวกับคนๆ นี้
            LaunchedEffect(patientId) {
                if (patientId != null) {
                    try {
                        isLoading = true
                        // สมมติว่าสร้าง getPatientById เพิ่มใน apiService แล้ว
                        patient = apiService.getPatients().find { it.patientId == patientId }
                        activeQuestions = apiService.getQuestions().filter { it.active }.sortedBy { it.displayOrder }

                        val records = apiService.getPatientRecords(patientId)
                        latestRecord = records.maxByOrNull { it.createdAt }

                        if (latestRecord != null) {
                            recordAnswers = apiService.getRecordAnswers(latestRecord!!.recordId!!)
                        }
                    } catch (e: Exception) {
                        println("Error loading details: ${e.message}")
                    } finally {
                        isLoading = false
                    }
                }
            }


            Text("รายละเอียดแบบคัดกรอง (Patient ID: $patientId)", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // 🌟 1. ดึงค่าจาก State มาเก็บในตัวแปร val ธรรมดาก่อน
            val currentPatient = patient
            val currentRecord = latestRecord

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                // 🌟 2. เปลี่ยนมาเช็ค null จากตัวแปร val ที่เราสร้างไว้แทน
                if (currentPatient == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ไม่พบข้อมูลผู้ป่วย", color = MaterialTheme.colorScheme.error)
                    }
                } else if (currentRecord == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ผู้ป่วยรายนี้ยังไม่เคยทำแบบประเมิน", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // 🌟 3. ส่งตัวแปรที่ถูก Smart Cast (มองว่าไม่ null แล้ว) เข้าไปใช้งาน
                            PatientInfoHeader(patient = currentPatient, record = currentRecord)
                            Divider(modifier = Modifier.padding(vertical = 16.dp))
                            Text(
                                text = "ประวัติการตอบคำถาม (ฉบับล่าสุด)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(activeQuestions) { question ->
                            val answer = recordAnswers.find { it.questionId == question.questionId }
                            QuestionAnswerItem(question = question, answer = answer)
                        }
                    }
                }
            }

            // ปุ่ม Action ด้านล่างสุดของโหมดดูรายละเอียด
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        // 🌟 1. ทำ Snapshot ดึงค่ามาเก็บไว้ใน val เฉพาะตอนที่กดปุ่ม
                        val currentPatient = patient
                        val currentRecord = latestRecord

                        // 🌟 2. เช็คจากตัวแปร Snapshot
                        if (currentPatient != null && currentRecord != null) {
                            val printItems = activeQuestions.map { question ->
                                val answer = recordAnswers.find { it.questionId == question.questionId }
                                PrintAnswerItem(
                                    order = question.displayOrder,
                                    question = question.questionText,
                                    isYes = answer?.answer ?: false,
                                    remark = answer?.remark
                                )
                            }
                            // 🌟 3. เรียกใช้งานได้เลยโดยไม่ติด Error
                            val formattedDate = currentRecord.createdAt.toString()

                            printVaccineDocument(
                                patientId = currentPatient.patientId.toString(),
                                fullName = "${currentPatient.firstName} ${currentPatient.lastName}",
                                date = formattedDate,
                                answers = printItems
                            )
                        }
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text("พิมพ์ใบคัดกรอง")
                }

                Button(
                    onClick = {
                        // TODO (API): POST ข้อมูลเข้า Vaccination_Record เพื่อตัด Lot วัคซีน
                        // ใช้ค่าจาก Parameter ได้ทันที
                        println("บันทึกการฉีดวัคซีนของผู้ป่วย ${patient?.patientId} ด้วย Lot $lotInput โดยเจ้าหน้าที่ $employeeRateId")
                    }
                ) {
                    Text("ยืนยันได้รับวัคซีนจริง")
                }
            }
        }
    }
}

@Composable
fun PatientInfoHeader(patient: Patient, record: ScreeningRecord) {
    Column {
        Text(text = "ชื่อ-นามสกุล: ${patient.firstName} ${patient.lastName}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "รหัสผู้ป่วย: ${patient.patientId}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))

        // แสดงวันที่ทำแบบประเมิน
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "ประเมินเมื่อ: ${record.createdAt}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun QuestionAnswerItem(question: ScreeningQuestion, answer: ScreeningAnswer?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // แก้ไขตรงนี้: เรียกใช้ clip() ตรงๆ
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)) // แก้ไขตรงนี้: เรียกใช้ background() ตรงๆ
            .padding(12.dp)
    ) {
        // ตัวคำถาม
        Text(
            text = "${question.displayOrder}. ${question.questionText}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // การแสดงผลคำตอบ
        if (answer == null) {
            Text(text = "ยังไม่ได้ระบุคำตอบ", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        } else {
            val answerText = if (answer.answer) "มี / ใช่" else "ไม่มี / ไม่ใช่"
            val answerColor = if (answer.answer) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = answerText,
                    color = answerColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (!answer.remark.isNullOrBlank()) {
                    Text(
                        text = " — ระบุ: ${answer.remark}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}