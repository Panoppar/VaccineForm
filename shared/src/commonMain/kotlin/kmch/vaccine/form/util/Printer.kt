package kmch.vaccine.form.util

data class PrintPatientInfo(
    val firstName: String,
    val lastName: String,
    val idCard: String?,
    val passportId: String?,
    val underlyingDisease: String?,
    val address: String?,
    val telNo: String,
    val shotDate: String
)

data class PrintAnswerItem(
    val order: Int,
    val question: String,
    val isYes: Boolean,
    val remark: String?
)

expect fun printVaccineDocument(
    patient: PrintPatientInfo,
    answers: List<PrintAnswerItem>
)
