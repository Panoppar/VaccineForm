package kmch.vaccine.form.domain.model

data class ScreeningAnswer(
    val questionId: Int,
    val answer: Boolean,
    val remark: String? = null
)
