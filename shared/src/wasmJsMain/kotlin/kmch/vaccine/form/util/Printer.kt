package kmch.vaccine.form.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// 1. สร้าง Data Class ที่ Serialize ได้สำหรับส่งไป JS
@Serializable
private data class JsAnswerItem(
    val order: Int,
    val question: String,
    val isYes: Boolean,
    val remark: String?
)

// 2. ใช้ @JsFun เพื่อประกาศฟังก์ชันสะพาน (Bridge) เชื่อมไปหา JavaScript
// โค้ดในวงเล็บคือโค้ด JavaScript แท้ๆ ที่จะไปเรียก printHelper.js ของเราอีกที
@JsFun("function(patientId, fullName, date, jsonString) { window.generateAndPrintVaccineForm(patientId, fullName, date, jsonString); }")
private external fun jsPrintVaccineForm(patientId: String, fullName: String, date: String, jsonString: String)

// 3. Implement actual function สำหรับ WasmJS
actual fun printVaccineDocument(
    patientId: String,
    fullName: String,
    date: String,
    answers: List<PrintAnswerItem>
) {
    // แมปข้อมูลให้อยู่ในรูปแบบที่พร้อมแปลงเป็น JSON
    val jsAnswers = answers.map {
        JsAnswerItem(it.order, it.question, it.isYes, it.remark)
    }

    // แปลงเป็น JSON String
    val jsonString = Json.encodeToString(jsAnswers)

    try {
        // เรียกใช้ฟังก์ชัน external ที่เชื่อมกับ JavaScript ไว้
        jsPrintVaccineForm(patientId, fullName, date, jsonString)
    } catch (e: Exception) {
        println("เกิดข้อผิดพลาดในการพิมพ์ (WasmJS): ${e.message}")
    }
}