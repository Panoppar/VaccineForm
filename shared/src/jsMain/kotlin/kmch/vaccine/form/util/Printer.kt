package kmch.vaccine.form.util

import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
private data class JsPatientInfo(
    val prefix: String,
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

actual fun printVaccineDocument(
    patient: PrintPatientInfo,
    answers: List<PrintAnswerItem>
) {
    val jsPatient = JsPatientInfo(
        prefix = patient.prefix,
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
    val dynamicWindow = window.asDynamic()

    try {
        // เรียกฟังก์ชันที่อยู่ใน printHelper.js
        dynamicWindow.generateAndPrintVaccineForm(patientJson, answersJson)
    } catch (e: Exception) {
        console.error("พิมพ์ไม่สำเร็จ: ${e.message}")
    }
}
