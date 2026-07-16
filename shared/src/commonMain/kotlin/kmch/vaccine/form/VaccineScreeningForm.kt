package kmch.vaccine.form

import androidx.compose.animation.AnimatedVisibility
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

@Composable
fun VaccineScreeningForm() {
    val apiService = remember { VaccineApiService() }
    val coroutineScope = rememberCoroutineScope()

    // 1. State สำหรับเก็บคำถามจาก API
    var questionList by remember { mutableStateOf<List<ScreeningQuestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var isSubmitted by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }

    val answers = remember { mutableStateMapOf<Int, Boolean>() }
    val remarks = remember { mutableStateMapOf<Int, String>() }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    // 2. ดึงคำถามเมื่อโหลดหน้าฟอร์ม
    LaunchedEffect(Unit) {
        try {
            questionList =
                apiService.getQuestions().filter { it.active }.sortedBy { it.displayOrder }
        } catch (e: Exception) {
            println("Error fetching questions: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // 3. เปลี่ยนจาก mockQuestions เป็น questionList
    val isAllQuestionsAnswered = questionList.all { question ->
        val isTextQuestion = question.questionText.startsWith("[Text]")
        if (isTextQuestion) {
            !remarks[question.questionId].isNullOrBlank()
        } else {
            answers.containsKey(question.questionId)
        }
    }

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

                if (questionList.isEmpty()) {
                    // กรณีโหลดเสร็จแล้วแต่ไม่มีข้อมูลคำถามกลับมาเลยจาก API
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
                    // วนลูปสร้างคำถามจากข้อมูลจริงที่ดึงมาจาก API (ซึ่งเรียงลำดับมาแล้วตั้งแต่ขั้นตอนโหลดข้อมูล)
                    questionList.forEach { question ->
                        // 1. จัดการ ID ให้ปลอดภัย ป้องกันกรณี Backend ส่งค่า null มา
                        val qId = question.questionId ?: return@forEach // หรือใช้ ?: 0 ตามความเหมาะสมของโมเดล

                        // 2. ตรวจสอบประเภทคำถามอย่างปลอดภัย
                        val rawText = question.questionText ?: ""
                        val isTextQuestion = rawText.startsWith("[Text]")

                        // ลบคำว่า [Text] ออกเพื่อแสดงผลได้อย่างสวยงาม
                        val displayText = if (isTextQuestion) {
                            rawText.removePrefix("[Text]").trim()
                        } else {
                            rawText
                        }

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
                                    // ==========================================
                                    // กรณีที่ 1: เป็นคำถามแบบ [Text] (ช่องพิมพ์คำตอบ)
                                    // ==========================================
                                    OutlinedTextField(
                                        value = remarks[qId] ?: "",
                                        onValueChange = { newValue ->
                                            remarks[qId] = newValue // นำ 'as Int' ออกแล้ว เพราะใช้ qId ที่ปลอดภัยแทน
                                        },
                                        label = { Text("ระบุคำตอบของคุณ") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = false,
                                        maxLines = 3
                                    )
                                } else {
                                    // ==========================================
                                    // กรณีที่ 2: เป็นคำถามปกติ (ตัวเลือก ใช่ / ไม่ใช่)
                                    // ==========================================
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = answers[qId] == true,
                                            onClick = {
                                                answers[qId] = true
                                            }
                                        )
                                        Text("มี / ใช่")
                                        Spacer(modifier = Modifier.width(24.dp))

                                        RadioButton(
                                            selected = answers[qId] == false,
                                            onClick = {
                                                answers[qId] = false
                                                // เคลียร์ค่า remark เมื่อผู้ใช้เปลี่ยนใจตอบว่า "ไม่ใช่"
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
                    verticalAlignment = Alignment.Top, // ปรับให้ Checkbox อยู่ขนานกับบรรทัดแรกของข้อความ
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        // เพิ่ม padding ให้ตัว Checkbox ไม่ชิดข้อความเกินไป
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // สร้าง AnnotatedString เพื่อประกอบข้อความและฝัง Link
                    val pdpaAnnotatedString = buildAnnotatedString {
                        append("โรงพยาบาลพระจอมเกล้าเจ้าคุณทหาร สถาบันเทคโนโลยีพระจอมเกล้าเจ้าคุณทหารลาดกระบัง (KMITL) มีการเก็บรวบรวม ใช้ และเปิดเผยข้อมูลส่วนบุคคลของท่าน ประกอบด้วย ชื่อ นามสกุล อีเมล เลขบัตรประจำตัวประชาชน อายุ หมายเลขโทรศัพท์ คณะ ข้อมูลสุขภาพ พฤติกรรม และสุขภาวะแวดล้อม เพื่อดำเนินการ ประสานงาน รวมถึงติดต่อกรณีที่มีอันจำเป็นภายหลังการตรวจสุขภาพ ในโครงการตรวจสุขภาพนักศึกษา ประจำปีงบประมาณ 2569 โดยใช้ “ฐานสัญญาและฐานความจำเป็นเพื่อประโยชน์โดยชอบด้วยกฎหมาย”\n\n")
                        append("ทางโรงพยาบาลยืนยันว่าข้อมูลส่วนบุคคลของท่านจะถูกเก็บเป็นความลับ และไม่มีการเปิดเผยโดยปราศจากความยินยอมของท่าน เว้นแต่เป็นการเปิดเผยตามที่กฎหมายกำหนด หรือตามหน้าที่ หรือเมื่อมีข้อบ่งชี้และความจำเป็นในการวินิจฉัย รักษาโรคและฟื้นฟูสภาพของข้าพเจ้า ซึ่งกรณีที่ตรวจพบความผิดปกติ ทางโรงพยาบาลจะมีการแจ้งผลให้ท่านทราบ เพื่อเป็นการติดตามและดูแลต่อไป\n\n")
                        append("หากมีข้อสงสัยเกี่ยวกับการคุ้มครองข้อมูลส่วนบุคคล สามารถสืบค้นข้อมูลเพิ่มเติมและช่องทางติดต่อได้ที่เว็บไซต์ ")

                        // เริ่มสร้าง Tag URL ให้กับข้อความส่วนนี้
                        pushStringAnnotation(
                            tag = "URL",
                            annotation = "https://pdpa.kmitl.ac.th"
                        )
                        // ตกแต่งข้อความส่วนที่เป็น Link ให้เป็นสี Primary และมีเส้นใต้
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("pdpa.kmitl.ac.th")
                        }
                        pop() // ปิด Tag
                    }

                    // ใช้ ClickableText แทน Text ธรรมดาเพื่อให้รับ Event การคลิกได้
                    ClickableText(
                        text = pdpaAnnotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface // บังคับสีพื้นฐานของข้อความให้กลืนไปกับ Theme ปัจจุบัน
                        ),
                        modifier = Modifier.padding(top = 12.dp), // ดันข้อความลงมานิดหน่อยให้ Center กับกล่อง Checkbox แถวแรก
                        onClick = { offset ->
                            // ตรวจสอบว่าจุดที่ผู้ใช้กดตรงกับข้อความที่แนบ tag "URL" ไว้หรือไม่
                            pdpaAnnotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let { annotation ->
                                    // ถ้าใช่ ให้ใช้ uriHandler สั่งเปิด Link ใน Browser
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            // 1. แปลง Map ของคำตอบและข้อความ ให้กลายเป็น List<ScreeningAnswerRequest>
                            val answerList = questionList.map { question ->
                                val qId = question.questionId ?: 0
                                val isTextQuestion = question.questionText.startsWith("[Text]")

                                ScreeningAnswerRequest(
                                    questionId = qId,
                                    // ถ้าเป็นคำถาม Text ปกติจะไม่มี Yes/No ให้บังคับเป็น false ไว้ก่อน (หรือตามที่ตกลงกับ Backend)
                                    // ถ้าเป็นคำถามปกติ ให้ดึงค่าจาก Map answers มาใช้ (ถ้าไม่มีค่า default เป็น false)
                                    answer = if (isTextQuestion) false else (answers[qId] == true),

                                    // ดึงข้อความจาก Map remarks (ถ้าไม่ได้พิมพ์จะเป็น null)
                                    remark = remarks[qId]
                                )
                            }

                            // 2. ประกอบร่างเป็น Request ก้อนใหญ่
                            val requestData = ScreeningSubmissionRequest(
                                patientId = null, // TODO: ระบุ ID ถ้ามี (ดึงจาก Parameter ของหน้าจอ)
                                patient = null,   // TODO: ถ้าระบุ ID ไม่ได้ ต้องสร้าง Object Patient ใส่เข้าไป
                                createdBy = "USER", // TODO: เปลี่ยนเป็นรหัสพนักงานหรือ "SELF"
                                answers = answerList
                            )

                            // 3. ยิง API ส่งให้ Golang Backend
                            try {
                                // เรียกใช้ API ที่เราเปิดใช้งานไว้
                                val isSuccess = apiService.submitScreeningForm(requestData)

                                if (isSuccess) {
                                    println("Data submitted successfully!")
                                    isSubmitted = true // เปลี่ยน State เพื่อแสดงหน้า Success
                                } else {
                                    println("Failed to submit data. API returned error status.")
                                    // คุณสามารถเพิ่ม State สำหรับโชว์ Snackbar หรือ Dialog แจ้งเตือนผู้ใช้ตรงนี้ได้
                                }
                            } catch (e: Exception) {
                                // จัดการ Error กรณี Network พัง หรือ Backend ล่ม
                                println("Network/Exception Error submitting form: ${e.cause}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = acceptedTerms && isAllQuestionsAnswered
                ) {
                    Text("ยืนยันบันทึกข้อมูล")
                }
            }
        }
    } else {
        // ... (โค้ดส่วนหน้าจอ Success เหมือนเดิม) ...
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
            Spacer(modifier = Modifier.height(24.dp))

//            Button(
//                onClick = { /* TODO: Generate PDF */ },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("ดาวน์โหลดใบคัดกรอง (PDF)")
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//            HorizontalDivider()
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text("หรือ ส่งไฟล์คัดกรองเข้าอีเมลเพื่อนำไปปริ้นท์")
//            Spacer(modifier = Modifier.height(16.dp))
//            OutlinedTextField(
//                value = emailInput,
//                onValueChange = { emailInput = it },
//                label = { Text("ระบุ Email Address") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = { /* TODO: Send Email API */ },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("ส่งเข้า Email")
//            }
        }
    }
}