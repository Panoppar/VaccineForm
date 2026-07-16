package kmch.vaccine.form.util

data class PrintAnswerItem(
    val order: Int,
    val question: String,
    val isYes: Boolean,
    val remark: String?
)

expect fun printVaccineDocument(
    patientId: String,
    fullName: String,
    date: String,
    answers: List<PrintAnswerItem>
)