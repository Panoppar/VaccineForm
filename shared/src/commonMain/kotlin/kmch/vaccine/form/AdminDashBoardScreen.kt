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
import kmch.vaccine.form.util.PrintPatientInfo
import kmch.vaccine.form.util.printVaccineDocument
import kotlinx.coroutines.delay
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

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

    val apiService = remember { VaccineApiService() }
    var registrationsList by remember { mutableStateOf<List<RegistrationListItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddVaccineDialog by remember { mutableStateOf(false) }

    // ดึงรายการจาก GET /api/v1/registrations?q=... ทุกครั้งที่ผู้ใช้พิมพ์ค้นหา (หน่วงเวลาเล็กน้อยกันยิง API รัว)
    LaunchedEffect(searchQuery) {
        delay(300)
        isLoading = true
        try {
            registrationsList = apiService.listRegistrations(query = searchQuery.ifBlank { null }).items
        } catch (e: Exception) {
            println("Error fetching registrations: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // ส่วนเรียกใช้ Dialog (จะทำงานเมื่อ showAddVaccineDialog เป็น true)
    if (showAddVaccineDialog) {
        AddVaccineOrLotDialog(
            onDismiss = { showAddVaccineDialog = false },
            onSaveVaccine = { newVaccineName ->
                // TODO: เรียก API POST /api/v1/vaccines พร้อมพารามิเตอร์ vaccine_name
                println("บันทึกวัคซีนใหม่: $newVaccineName")
            },
            onSaveLot = { refVaccineId, newLotNumber, initialQty ->
                // TODO: เรียก API POST /api/v1/vaccine-lots พร้อมพารามิเตอร์ vaccine_id, lot_number, initial_quantity
                // หมายเหตุ: API ควรเซ็ต remaining_quantity = initial_quantity ในฝั่ง Backend
                println("บันทึกล็อตใหม่: วัคซีน ID $refVaccineId, Lot $newLotNumber, จำนวน $initialQty")
            }
        )
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically // เพื่อให้ Field และ ปุ่ม + อยู่กึ่งกลางตรงกัน
            ) {
                // จัดกลุ่ม Field ของ Lot และปุ่ม + เข้าด้วยกัน
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = lotInput,
                        onValueChange = onLotInputChange,
                        label = { Text("Lot วัคซีน") },
                        modifier = Modifier.width(160.dp) // ปรับขนาดลงเล็กน้อยเพื่อให้พอดีกับปุ่ม
                    )
                    IconButton(
                        onClick = { showAddVaccineDialog = true },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "เพิ่มวัคซีนหรือล็อตใหม่",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                OutlinedTextField(
                    value = employeeRateId,
                    onValueChange = onEmployeeRateIdChange,
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
                    label = { Text("ค้นหาชื่อ/เลขบัตร/เบอร์โทร") }
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
        } else if (registrationsList.isEmpty()) {
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
                items(registrationsList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPatientSelected(item.patientId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("รหัส: ${item.patientId}", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "ชื่อ-นามสกุล: ${item.prefix}${item.firstName} ${item.lastName}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text("เบอร์โทร: ${item.telNo}")
                            Text("วันที่รับวัคซีน: ${item.shotDate}")
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
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = onBack) {
            Text("< กลับหน้ารายการคัดกรอง")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isNew) {
            Text("กรอกแบบคัดกรองใหม่โดยเจ้าหน้าที่", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // VaccineScreeningForm ยิง POST /api/v1/registrations ให้เองเมื่อกดยืนยันบันทึกข้อมูลในฟอร์ม
            VaccineScreeningForm()
        } else { // กรณีดูรายละเอียดผู้ป่วย (isNew == false)
            val apiService = remember { VaccineApiService() }
            var detail by remember { mutableStateOf<RegistrationDetail?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var loadError by remember { mutableStateOf<String?>(null) }

            // ยิง GET /api/v1/registrations/:patient_id
            LaunchedEffect(patientId) {
                if (patientId != null) {
                    isLoading = true
                    loadError = null
                    try {
                        detail = apiService.getRegistrationDetail(patientId)
                        if (detail == null) loadError = "ไม่พบข้อมูลผู้ป่วย"
                    } catch (e: Exception) {
                        loadError = "โหลดข้อมูลไม่สำเร็จ: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }

            Text("รายละเอียดแบบคัดกรอง (Patient ID: $patientId)", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            val currentDetail = detail

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (currentDetail == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(loadError ?: "ไม่พบข้อมูลผู้ป่วย", color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            PatientInfoHeader(detail = currentDetail)
                            Divider(modifier = Modifier.padding(vertical = 16.dp))
                            Text(
                                text = "ประวัติการตอบคำถาม",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(currentDetail.answers) { answer ->
                            QuestionAnswerItem(answer = answer)
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
                        val snapshot = detail
                        if (snapshot != null) {
                            val printItems = snapshot.answers.mapIndexed { index, answer ->
                                PrintAnswerItem(
                                    order = index + 1,
                                    question = answer.questionText,
                                    isYes = answer.answer,
                                    remark = answer.remark
                                )
                            }
                            printVaccineDocument(
                                patient = PrintPatientInfo(
                                    firstName = snapshot.firstName,
                                    lastName = snapshot.lastName,
                                    idCard = snapshot.idCard,
                                    passportId = snapshot.passportId,
                                    underlyingDisease = snapshot.underlyingDisease,
                                    address = snapshot.address,
                                    telNo = snapshot.telNo,
                                    shotDate = snapshot.shotDate.toString()
                                ),
                                answers = printItems
                            )
                        }
                    },
                    modifier = Modifier.padding(end = 16.dp),
                    enabled = detail != null
                ) {
                    Text("พิมพ์ใบคัดกรอง")
                }

                Button(
                    onClick = {
                        // TODO (API): ยังไม่มี endpoint แยกสำหรับยืนยันฉีดจริง — POST /registrations สร้าง vaccination record ให้แล้วในขั้นตอนลงทะเบียน
                        println("บันทึกการฉีดวัคซีนของผู้ป่วย $patientId ด้วย Lot $lotInput โดยเจ้าหน้าที่ $employeeRateId")
                    }
                ) {
                    Text("ยืนยันได้รับวัคซีนจริง")
                }
            }
        }
    }
}

@Composable
fun PatientInfoHeader(detail: RegistrationDetail) {
    Column {
        Text(
            text = "ชื่อ-นามสกุล: ${detail.prefix}${detail.firstName} ${detail.lastName}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "รหัสผู้ป่วย: ${detail.patientId}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "เบอร์โทร: ${detail.telNo}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!detail.underlyingDisease.isNullOrBlank()) {
            Text(
                text = "โรคประจำตัว: ${detail.underlyingDisease}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "วันที่รับวัคซีน: ${detail.shotDate}" +
                    (detail.vaccineName?.let { " • $it" } ?: "") +
                    (detail.lotNumber?.let { " • Lot $it" } ?: ""),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun QuestionAnswerItem(answer: RegistrationAnswerDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Text(
            text = answer.questionText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

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

enum class AddMode { VACCINE, LOT }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccineOrLotDialog(
    onDismiss: () -> Unit,
    onSaveVaccine: (vaccineName: String) -> Unit,
    onSaveLot: (vaccineId: String, lotNumber: String, initialQty: Int) -> Unit
) {
    var selectedMode by remember { mutableStateOf(AddMode.VACCINE) }

    // State สำหรับโหมดเพิ่มวัคซีน
    var vaccineName by remember { mutableStateOf("") }

    // State สำหรับโหมดเพิ่มล็อต (ในระบบจริง vaccineId ควรปรับเป็น Dropdown Menu ที่ดึงข้อมูลจาก API)
    var vaccineId by remember { mutableStateOf("") }
    var lotNumber by remember { mutableStateOf("") }
    var initialQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "จัดการข้อมูลวัคซีน", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Radio Buttons สำหรับเลือกประเภทการทำรายการ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = selectedMode == AddMode.VACCINE,
                        onClick = { selectedMode = AddMode.VACCINE }
                    )
                    Text("เพิ่มวัคซีนใหม่", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.width(8.dp))

                    RadioButton(
                        selected = selectedMode == AddMode.LOT,
                        onClick = { selectedMode = AddMode.LOT }
                    )
                    Text("เพิ่มล็อตวัคซีน", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // สลับ Input Field ตาม State
                if (selectedMode == AddMode.VACCINE) {
                    OutlinedTextField(
                        value = vaccineName,
                        onValueChange = { vaccineName = it },
                        label = { Text("ชื่อวัคซีน (Vaccine Name)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    OutlinedTextField(
                        value = vaccineId,
                        onValueChange = { vaccineId = it },
                        label = { Text("รหัสอ้างอิงวัคซีน (Vaccine ID)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lotNumber,
                        onValueChange = { lotNumber = it },
                        label = { Text("หมายเลขล็อต (Lot Number)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = initialQuantity,
                        onValueChange = { initialQuantity = it },
                        label = { Text("จำนวนเริ่มต้น (Initial Quantity)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedMode == AddMode.VACCINE) {
                        onSaveVaccine(vaccineName)
                    } else {
                        val qty = initialQuantity.toIntOrNull() ?: 0
                        onSaveLot(vaccineId, lotNumber, qty)
                    }
                    onDismiss()
                },
                enabled = if (selectedMode == AddMode.VACCINE) {
                    vaccineName.isNotBlank()
                } else {
                    vaccineId.isNotBlank() && lotNumber.isNotBlank() && initialQuantity.isNotBlank()
                }
            ) {
                Text("บันทึก")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ยกเลิก")
            }
        }
    )
}
