package kmch.vaccine.form.domain.model

data class ScreeningQuestion(
    val questionId: Int,
    val questionText: String,
    val displayOrder: Int
) {
    val isTextQuestion: Boolean get() = questionText.startsWith("[Text]")
    val displayText: String get() = if (isTextQuestion) questionText.removePrefix("[Text]").trim() else questionText
}
