package kmch.vaccine.form.presentation.viewmodel

import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.RegistrationConfirmation
import kmch.vaccine.form.domain.model.ScreeningQuestion
import kmch.vaccine.form.domain.model.Sex
import kotlinx.datetime.LocalDate

data class ScreeningFormUiState(
    val prefix: Prefix? = null,
    val firstName: String = "",
    val lastName: String = "",
    val sex: Sex? = null,
    val patientType: PatientType? = null,
    val documentType: DocumentType = DocumentType.ID_CARD,
    val idCard: String = "",
    val passportId: String = "",
    val age: String = "",
    val telNo: String = "",
    val underlyingDisease: String = "",
    val address: String = "",
    val zipCode: String = "",
    val shotDate: String = "",
    val questionList: List<ScreeningQuestion> = emptyList(),
    val answers: Map<Int, Boolean> = emptyMap(),
    val remarks: Map<Int, String> = emptyMap(),
    val acceptedTerms: Boolean = false,
    val acceptedVaccineInfo: Boolean = false,
    val isLoadingQuestions: Boolean = true,
    val loadError: DomainError? = null,
    val isSubmitting: Boolean = false,
    val submitError: DomainError? = null,
    val isSubmitted: Boolean = false,
    val successResponse: RegistrationConfirmation? = null
) {
    val isDocumentValid: Boolean
        get() = when (documentType) {
            DocumentType.ID_CARD -> idCard.length == 13
            DocumentType.PASSPORT -> passportId.isNotBlank()
        }

    val isPhoneValid: Boolean get() = telNo.length == 10
    val parsedAge: Int? get() = age.toIntOrNull()
    val parsedShotDate: LocalDate? get() = runCatching { LocalDate.parse(shotDate) }.getOrNull()

    val isPatientInfoValid: Boolean
        get() = prefix != null && firstName.isNotBlank() && lastName.isNotBlank() &&
                sex != null && patientType != null && isDocumentValid &&
                (parsedAge?.let { it > 0 } == true) && isPhoneValid &&
                address.isNotBlank() && parsedShotDate != null

    val isAllQuestionsAnswered: Boolean
        get() = questionList.all { question ->
            if (question.isTextQuestion) {
                !remarks[question.questionId].isNullOrBlank()
            } else {
                answers.containsKey(question.questionId)
            }
        }

    val canSubmit: Boolean
        get() = acceptedTerms && acceptedVaccineInfo && isAllQuestionsAnswered &&
                isPatientInfoValid && !isSubmitting
}
