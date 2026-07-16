package kmch.vaccine.form.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
private data class JsPatientInfo(
    val firstName: String,
    val lastName: String,
    val idCard: String?,
    val passportId: String?,
    val underlyingDisease: String?,
    val address: String?,
    val telNo: String,
    val shotDate: String
)

@Serializable
private data class JsAnswerItem(
    val order: Int,
    val question: String,
    val isYes: Boolean,
    val remark: String?
)

// ใช้ @JsFun เพื่อประกาศฟังก์ชันสะพาน (Bridge) เชื่อมไปหา JavaScript
// โค้ดในวงเล็บคือโค้ด JavaScript แท้ๆ ที่จะไปเรียก printHelper.js ของเราอีกที
@JsFun("function(patientJson, answersJson) { window.generateAndPrintVaccineForm(patientJson, answersJson); }")
private external fun jsPrintVaccineForm(patientJson: String, answersJson: String)

actual fun printVaccineDocument(
    patient: PrintPatientInfo,
    answers: List<PrintAnswerItem>
) {
    val jsPatient = JsPatientInfo(
        firstName = patient.firstName,
        lastName = patient.lastName,
        idCard = patient.idCard,
        passportId = patient.passportId,
        underlyingDisease = patient.underlyingDisease,
        address = patient.address,
        telNo = patient.telNo,
        shotDate = patient.shotDate
    )
    val jsAnswers = answers.map { JsAnswerItem(it.order, it.question, it.isYes, it.remark) }

    val patientJson = Json.encodeToString(jsPatient)
    val answersJson = Json.encodeToString(jsAnswers)

    try {
        jsPrintVaccineForm(patientJson, answersJson)
    } catch (e: Exception) {
        println("เกิดข้อผิดพลาดในการพิมพ์ (WasmJS): ${e.message}")
    }
}
