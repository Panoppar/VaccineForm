package kmch.vaccine.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VaccineScreeningForm() {
    val scrollState = rememberScrollState()

    // --- State Variables ---
    // 1. ข้อมูลส่วนตัว
    var prefix by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var idCard by remember { mutableStateOf("") }

    // 2. โรคประจำตัว
    val diseasesList = listOf("เบาหวาน", "ความดันโลหิตสูง", "โรคเกาต์", "ไตวายเรื้อรัง", "กล้ามเนื้อหัวใจขาดเลือด", "หลอดเลือดสมอง", "ถุงลมโป่งพอง", "โรคทางจิตเวช", "อื่นๆ")
    var selectedDiseases by remember { mutableStateOf(setOf<String>()) }
    var otherDiseaseText by remember { mutableStateOf("") }

    // 3. ที่อยู่ (อิงตาม Table Address)
    var houseNo by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var buildingName by remember { mutableStateOf("") }
    var alley by remember { mutableStateOf("") }
    var road by remember { mutableStateOf("") }
    var subDistrict by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }

    // 4. การติดต่อและการรับบริการ
    var telNo by remember { mutableStateOf("") }
    var shotDate by remember { mutableStateOf("") }

    // 5. แบบสอบถาม (Questionnaire) - ใช้ Boolean? (null = ยังไม่เลือก, true = ใช่, false = ไม่ใช่)
    var qEggAllergy by remember { mutableStateOf<Boolean?>(null) }
    var qFluShotAllergy by remember { mutableStateOf<Boolean?>(null) }
    var qHasFever by remember { mutableStateOf<Boolean?>(null) }
    var qAcuteSevenDays by remember { mutableStateOf<Boolean?>(null) }
    var qDischargeFourteenDays by remember { mutableStateOf<Boolean?>(null) }
    var qUnderlyingWorsen by remember { mutableStateOf<Boolean?>(null) }
    var qHighRiskPregnancy by remember { mutableStateOf<Boolean?>(null) }
    var isUnderstand by remember { mutableStateOf(false) } // เช็คบ็อกซ์รับทราบข้อมูล

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("แบบฟอร์มคัดกรองผู้มารับบริการวัคซีนไข้หวัดใหญ่", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // 1. คำนำหน้า (Single Select Chip)
        Text("คำนำหน้าชื่อ", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("นาย", "นาง", "นางสาว").forEach { option ->
                FilterChip(
                    selected = prefix == option,
                    onClick = { prefix = option },
                    label = { Text(option) }
                )
            }
        }

        // 2 & 3. ชื่อ - นามสกุล
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("ชื่อ") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("นามสกุล") },
            modifier = Modifier.fillMaxWidth()
        )

        // 4. เพศแต่กำเนิด (Single Select Chip)
        Text("เพศแต่กำเนิด", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ชาย", "หญิง").forEach { option ->
                FilterChip(
                    selected = sex == option,
                    onClick = { sex = option },
                    label = { Text(option) }
                )
            }
        }

        // 5. เลขบัตรประชาชน
        OutlinedTextField(
            value = idCard,
            onValueChange = { if (it.length <= 13) idCard = it },
            label = { Text("เลขประจำตัวประชาชน 13 หลัก") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 6. โรคประจำตัว (Multi Select Chip)
        Text("โรคประจำตัว (เลือกได้มากกว่า 1 ข้อ)", fontWeight = FontWeight.Bold)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            diseasesList.forEach { disease ->
                FilterChip(
                    selected = selectedDiseases.contains(disease),
                    onClick = {
                        val current = selectedDiseases.toMutableSet()
                        if (current.contains(disease)) current.remove(disease)
                        else current.add(disease)
                        selectedDiseases = current
                    },
                    label = { Text(disease) }
                )
            }
        }
        // เงื่อนไข: ถ้าเลือกอื่นๆ ให้แสดงช่องกรอก
        if (selectedDiseases.contains("อื่นๆ")) {
            OutlinedTextField(
                value = otherDiseaseText,
                onValueChange = { otherDiseaseText = it },
                label = { Text("โปรดระบุโรคประจำตัวอื่นๆ") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 7. ที่อยู่ (Address Fields)
        Text("ข้อมูลที่อยู่", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = houseNo, onValueChange = { houseNo = it }, label = { Text("บ้านเลขที่") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = village, onValueChange = { village = it }, label = { Text("หมู่ที่") }, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(value = buildingName, onValueChange = { buildingName = it }, label = { Text("หมู่บ้าน/อาคาร") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = alley, onValueChange = { alley = it }, label = { Text("ตรอก/ซอย") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = road, onValueChange = { road = it }, label = { Text("ถนน") }, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = subDistrict, onValueChange = { subDistrict = it }, label = { Text("ตำบล/แขวง") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = district, onValueChange = { district = it }, label = { Text("อำเภอ/เขต") }, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = province, onValueChange = { province = it }, label = { Text("จังหวัด") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = zipCode, onValueChange = { zipCode = it }, label = { Text("รหัสไปรษณีย์") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 8. เบอร์โทรศัพท์ & 9. วันที่รับวัคซีน
        OutlinedTextField(
            value = telNo,
            onValueChange = { telNo = it },
            label = { Text("เบอร์โทรศัพท์") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = shotDate,
            onValueChange = { shotDate = it },
            label = { Text("วันที่รับวัคซีน (เช่น DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 10. แบบสอบถามคัดกรอง (Questionnaire)
        Text("แบบสอบถามคัดกรองก่อนให้วัคซีน", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        QuestionRow("มีประวัติแพ้ไข่ไก่อย่างรุนแรง", qEggAllergy) { qEggAllergy = it }
        QuestionRow("เคยแพ้วัคซีนไข้หวัดใหญ่ หรือแพ้สารประกอบอื่นๆ ในวัคซีนอย่างรุนแรง", qFluShotAllergy) { qFluShotAllergy = it }
        QuestionRow("กำลังมีไข้ หรือกำลังเจ็บป่วยเฉียบพลัน", qHasFever) { qHasFever = it }
        QuestionRow("เพิ่งหายจากการเจ็บป่วยเฉียบพลันมาไม่เกิน 7 วัน", qAcuteSevenDays) { qAcuteSevenDays = it }
        QuestionRow("เพิ่งมานอนรักษาตัวและออกจากโรงพยาบาล ไม่เกิน 14 วัน", qDischargeFourteenDays) { qDischargeFourteenDays = it }
        QuestionRow("ยังมีโรคประจำตัวที่อาการกำเริบ เช่น ใจสั่น เจ็บแน่นหน้าอก หรือยังควบคุมอาการไม่ได้", qUnderlyingWorsen) { qUnderlyingWorsen = it }
        QuestionRow("ขณะตั้งครรภ์มีภาวะครรภ์เสี่ยงสูง (หากประสงค์รับวัคซีนให้ปรึกษาแพทย์)", qHighRiskPregnancy) { qHighRiskPregnancy = it }

        // Checkbox รับทราบข้อมูล
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isUnderstand, onCheckedChange = { isUnderstand = it })
            Text("ท่านได้รับข้อมูลเกี่ยวกับวัคซีนไข้หวัดใหญ่และได้ทำความเข้าใจแล้ว", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ปุ่ม Submit & API TODO
        Button(
            onClick = {
                // TODO: 1. Validate ข้อมูลก่อนส่ง (เช่น เลขบัตร 13 หลัก, กรอกข้อมูลครบไหม)
                // TODO: 2. รวบรวม State ทั้งหมดแปลงเป็น JSON Object
                /*
                 val requestBody = PatientVaccineRequest(
                     patientInfo = PatientInfo(idCard, prefix, firstName, lastName, sex, telNo),
                     address = Address(idCard, houseNo, village, buildingName, alley, road, subDistrict, district, province, zipCode),
                     diseases = selectedDiseases.map { ... }, // จัดการรวมคำว่า "อื่นๆ: $otherDiseaseText" ด้วย
                     fluShotForm = FluShotScreenForm(idCard, qEggAllergy, qFluShotAllergy, ... , isUnderstand, shotDate)
                 )
                */
                // TODO: 3. ยิง API (POST) ไปยัง Backend Golang ผ่าน Ktor Client หรือไลบรารี Network ที่คุณใช้
                println("Submitting Data... (Call Golang API here)")
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("บันทึกข้อมูล", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(32.dp)) // เผื่อที่ว่างด้านล่างสำหรับจอโทรศัพท์
    }
}

// Helper Composable สำหรับสร้างแถวคำถาม ใช่/ไม่ใช่
@Composable
fun QuestionRow(question: String, selectedAnswer: Boolean?, onAnswerSelected: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = question, fontSize = 14.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedAnswer == true,
                    onClick = { onAnswerSelected(true) }
                )
                Text("ใช่")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedAnswer == false,
                    onClick = { onAnswerSelected(false) }
                )
                Text("ไม่ใช่")
            }
        }
    }
}