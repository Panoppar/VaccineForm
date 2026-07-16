package kmch.vaccine.form.util

import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
private data class JsAnswerItem(
    val order: Int,
    val question: String,
    val isYes: Boolean,
    val remark: String?
)

actual fun printVaccineDocument(
    patientId: String,
    fullName: String,
    date: String,
    answers: List<PrintAnswerItem>
) {
    val jsAnswers = answers.map {
        JsAnswerItem(it.order, it.question, it.isYes, it.remark)
    }

    val jsonString = Json.encodeToString(jsAnswers)
    val dynamicWindow = window.asDynamic()

    try {
        // เรียกฟังก์ชันที่อยู่ใน printHelper.js
        dynamicWindow.generateAndPrintVaccineForm(patientId, fullName, date, jsonString)
    } catch (e: Exception) {
        console.error("พิมพ์ไม่สำเร็จ: ${e.message}")
    }
}