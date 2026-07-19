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
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import kmch.vaccine.form.theme.successColors
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

@Composable
fun AdminDashBoardScreen(onNavigateBack: () -> Unit) {
    var selectedPatientId by remember { mutableStateOf<Int?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }

    // STATE HOISTING
    var lotInput by remember { mutableStateOf("") }
    var employeeRateId by remember { mutableStateOf("") }

    if (selectedPatientId != null || isCreatingNew) {
        AdminDetailView(
            patientId = selectedPatientId,
            isNew = isCreatingNew,
            lotInput = lotInput,
            employeeRateId = employeeRateId,
            onBack = {
                selectedPatientId = null
                isCreatingNew = false
            }
        )
    } else {
        AdminListView(
            lotInput = lotInput,
            onLotInputChange = { lotInput = it.filter { char -> char.isDigit() } }, // บังคับกรอกเป็นตัวเลข Lot ID
            employeeRateId = employeeRateId,
            onEmployeeRateIdChange = { employeeRateId = it },
            onPatientSelected = { selectedPatientId = it },
            onCreateNew = { isCreatingNew = true }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
    val coroutineScope = rememberCoroutineScope()

    var registrationsList by remember { mutableStateOf<List<RegistrationListItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddVaccineDialog by remember { mutableStateOf(false) }
    var dialogAlertMessage by remember { mutableStateOf<String?>(null) }

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

    if (dialogAlertMessage != null) {
        AlertDialog(
            onDismissRequest = { dialogAlertMessage = null },
            confirmButton = { TextButton(onClick = { dialogAlertMessage = null }) { Text("ตกลง") } },
            title = { Text("แจ้งเตือน") },
            text = { Text(dialogAlertMessage!!) }
        )
    }

    if (showAddVaccineDialog) {
        // (โค้ด AddVaccineOrLotDialog คงเดิม)
        AddVaccineOrLotDialog(
            onDismiss = { showAddVaccineDialog = false },
            onSaveVaccine = { newVaccineName ->
                coroutineScope.launch {
                    try {
                        val response = apiService.createVaccine(VaccineCreateRequest(vaccineName = newVaccineName))
                        dialogAlertMessage = "เพิ่มวัคซีนสำเร็จ (Vaccine ID: ${response.vaccineId})"
                        showAddVaccineDialog = false
                    } catch (e: Exception) {
                        dialogAlertMessage = "เกิดข้อผิดพลาด: ${e.message}"
                    }
                }
            },
            onSaveLot = { refVaccineId, newLotNumber, initialQty ->
                coroutineScope.launch {
                    try {
                        val vId = refVaccineId.toIntOrNull() ?: throw Exception("ID วัคซีนต้องเป็นตัวเลข")
                        val response = apiService.createLot(
                            vaccineId = vId,
                            request = LotCreateRequest(lotNumber = newLotNumber, initialQuantity = initialQty)
                        )
                        dialogAlertMessage = "เพิ่มล็อตสำเร็จ (Lot ID: ${response.lotId})"
                        showAddVaccineDialog = false
                    } catch (e: Exception) {
                        dialogAlertMessage = "เกิดข้อผิดพลาด: ${e.message}"
                    }
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) { // ลด padding ลงนิดหน่อยเพื่อให้เหมาะกับมือถือ
        Text("แดชบอร์ดจัดการคัดกรองวัคซีน", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // ใช้ FlowRow เพื่อให้ปัดบรรทัดอัตโนมัติเมื่อจอแคบ
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = lotInput,
                    onValueChange = onLotInputChange,
                    label = { Text("Lot ID (ตัวเลข)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(140.dp)
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
                modifier = Modifier.width(180.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("ค้นหาชื่อ/เลขบัตร/เบอร์โทร") },
                modifier = Modifier.weight(1f).widthIn(min = 200.dp) // ขยายให้เต็มพื้นที่ที่เหลือแต่กำหนดขั้นต่ำไว้
            )

            FloatingActionButton(onClick = onCreateNew) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (registrationsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "ไม่พบข้อมูลผู้รับบริการที่ตรงกับ '$searchQuery'",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(registrationsList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPatientSelected(item.patientId) }
                    ) {
                        // ปรับจาก Row ยาวๆ เป็น Column เพื่อไม่ให้ข้อความเบียดกันในมือถือ
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "รหัส: ${item.patientId}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "วันที่: ${item.shotDate}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "ชื่อ-นามสกุล: ${item.prefix}${item.firstName} ${item.lastName}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "เบอร์โทร: ${item.telNo}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
        } else {
            var detail by remember { mutableStateOf<RegistrationDetail?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var loadError by remember { mutableStateOf<String?>(null) }

            // State สำหรับสถานะการบันทึกฉีดวัคซีนจริง
            var isSubmittingRecord by remember { mutableStateOf(false) }
            var recordResultMsg by remember { mutableStateOf<String?>(null) }
            var isVaccinationConfirmed by remember { mutableStateOf(false) }
            val success = successColors()

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

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp) // เว้นระยะถ้าต้องปัดบรรทัด
            ) {
                Text("รายละเอียดแบบคัดกรอง (Patient ID: $patientId)", style = MaterialTheme.typography.headlineSmall)

                if (isVaccinationConfirmed) {
                    Surface(
                        color = success.successContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ยืนยันการลงทะเบียนเรียบร้อย",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = success.onSuccessContainer
                        )
                    }
                }
            }
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
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            PatientInfoHeader(detail = currentDetail, isConfirmed = isVaccinationConfirmed)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
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

            // แสดงแจ้งเตือนผลลัพธ์
            recordResultMsg?.let { msg ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = msg,
                    color = if (msg.contains("สำเร็จ")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
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
                                    prefix = snapshot.prefix,
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

                // ปิดใช้งานชั่วคราว: กดแล้วสร้าง Vaccination_Record แถวใหม่แยกต่างหากจากที่ auto-FIFO
                // สร้างไว้แล้วตอนลงทะเบียน (ดู POST /registrations ที่ return vaccinationId มาแล้ว)
                // กลายเป็นข้อมูลซ้ำซ้อน/ไม่ถูกต้องใน DB และฝั่ง DB เองก็ไม่มี field บอกสถานะ
                // "ยืนยันแล้ว" ให้เช็คได้จริง ต้องรอแก้ที่ Backend ก่อนถึงจะเปิดใช้ใหม่
                // Button(
                //     onClick = {
                //         val lId = lotInput.toIntOrNull()
                //         if (lId == null) {
                //             recordResultMsg = "กรุณาระบุ Lot ID (ตัวเลข) ในช่องด้านบนก่อนกดยืนยัน"
                //             return@Button
                //         }
                //         if (patientId == null || detail == null) return@Button
                //
                //         coroutineScope.launch {
                //             isSubmittingRecord = true
                //             recordResultMsg = null
                //             try {
                //                 apiService.createVaccinationRecord(
                //                     VaccinationRecordRequest(
                //                         patientId = patientId,
                //                         lotId = lId,
                //                         shotDate = detail!!.shotDate
                //                     )
                //                 )
                //                 recordResultMsg = "บันทึกการฉีดวัคซีนสำเร็จ! (Lot ID: $lId, เจ้าหน้าที่: $employeeRateId)"
                //                 isVaccinationConfirmed = true
                //             } catch (e: ApiException) {
                //                 recordResultMsg = when (e.statusCode) {
                //                     404 -> "ข้อผิดพลาด: ไม่พบผู้ป่วยหรือ Lot ไม่ถูกต้อง"
                //                     409 -> "ข้อผิดพลาด: วัคซีนใน Lot นี้หมดสต็อกแล้ว"
                //                     else -> "ข้อผิดพลาด (${e.statusCode}): ${e.message}"
                //                 }
                //             } catch (e: Exception) {
                //                 recordResultMsg = "ข้อผิดพลาดเครือข่าย: ${e.message}"
                //             } finally {
                //                 isSubmittingRecord = false
                //             }
                //         }
                //     },
                //     enabled = detail != null && !isSubmittingRecord
                // ) {
                //     Text(if (isSubmittingRecord) "กำลังบันทึก..." else "ยืนยันได้รับวัคซีนจริง")
                // }
            }
        }
    }
}

@Composable
fun PatientInfoHeader(detail: RegistrationDetail, isConfirmed: Boolean = false) {
    val success = successColors()
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
            color = if (isConfirmed) success.successContainer else MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "วันที่รับวัคซีน: ${detail.shotDate}" +
                        (detail.vaccineName?.let { " • $it" } ?: "") +
                        (detail.lotNumber?.let { " • Lot $it" } ?: "") +
                        (if (isConfirmed) " • ยืนยันแล้ว" else ""),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isConfirmed) FontWeight.Bold else FontWeight.Normal,
                color = if (isConfirmed) success.onSuccessContainer else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun QuestionAnswerItem(answer: RegistrationAnswerDetail) {
    // โค้ดเดิม ไม่ต้องแก้ไข
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

    var vaccineName by remember { mutableStateOf("") }
    var vaccineId by remember { mutableStateOf("") }
    var lotNumber by remember { mutableStateOf("") }
    var initialQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "จัดการข้อมูลวัคซีน", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

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
                        onValueChange = { newId -> vaccineId = newId.filter { it.isDigit() } },
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
                        onValueChange = { newQty -> initialQuantity = newQty.filter { it.isDigit() } },
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
                    // หมายเหตุ: ตัด onDismiss() ออกจากตรงนี้ แล้วให้ทำเมื่อ API Success แทนจากด้านบน (showAddVaccineDialog = false)
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