package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.RegistrationSubmission
import kmch.vaccine.form.domain.model.ScreeningAnswer
import kmch.vaccine.form.domain.model.Sex
import kmch.vaccine.form.domain.model.toSubmissionValue
import kmch.vaccine.form.domain.usecase.GetScreeningQuestionsUseCase
import kmch.vaccine.form.domain.usecase.SubmitRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

private fun todayDateString(): String = Clock.System.now().toString().substring(0, 10)

class ScreeningFormViewModel(
    private val getScreeningQuestionsUseCase: GetScreeningQuestionsUseCase,
    private val submitRegistrationUseCase: SubmitRegistrationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreeningFormUiState(shotDate = todayDateString()))
    val uiState: StateFlow<ScreeningFormUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingQuestions = true, loadError = null) }
            getScreeningQuestionsUseCase()
                .onSuccess { questions ->
                    _uiState.update { it.copy(questionList = questions, isLoadingQuestions = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(loadError = error as? DomainError ?: DomainError.NetworkError, isLoadingQuestions = false)
                    }
                }
        }
    }

    fun onPrefixChange(prefix: Prefix) = _uiState.update { it.copy(prefix = prefix) }
    fun onFirstNameChange(value: String) = _uiState.update { it.copy(firstName = value) }
    fun onLastNameChange(value: String) = _uiState.update { it.copy(lastName = value) }
    fun onSexChange(sex: Sex) = _uiState.update { it.copy(sex = sex) }
    fun onPatientTypeChange(type: PatientType) = _uiState.update { it.copy(patientType = type) }
    fun onDocumentTypeChange(type: DocumentType) = _uiState.update { it.copy(documentType = type) }
    fun onIdCardChange(value: String) = _uiState.update { it.copy(idCard = value.filter(Char::isDigit).take(13)) }
    fun onPassportIdChange(value: String) = _uiState.update { it.copy(passportId = value) }
    fun onAgeChange(value: String) = _uiState.update { it.copy(age = value.filter(Char::isDigit)) }
    fun onTelNoChange(value: String) = _uiState.update { it.copy(telNo = value.filter(Char::isDigit).take(10)) }
    fun onUnderlyingDiseaseChange(value: String) = _uiState.update { it.copy(underlyingDisease = value) }
    fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
    fun onZipCodeChange(value: String) = _uiState.update { it.copy(zipCode = value.filter(Char::isDigit)) }
    fun onShotDateChange(value: String) = _uiState.update { it.copy(shotDate = value) }
    fun onAcceptedTermsChange(value: Boolean) = _uiState.update { it.copy(acceptedTerms = value) }
    fun onAcceptedVaccineInfoChange(value: Boolean) = _uiState.update { it.copy(acceptedVaccineInfo = value) }

    fun onAnswerChange(questionId: Int, answer: Boolean) = _uiState.update {
        it.copy(
            answers = it.answers + (questionId to answer),
            // Selecting "No" clears any previously typed remark for that question.
            remarks = if (!answer) it.remarks - questionId else it.remarks
        )
    }

    fun onRemarkChange(questionId: Int, remark: String) = _uiState.update {
        it.copy(remarks = it.remarks + (questionId to remark))
    }

    fun submit() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitError = null) }

            val answerList = state.questionList.map { question ->
                ScreeningAnswer(
                    questionId = question.questionId,
                    answer = if (question.isTextQuestion) false else (state.answers[question.questionId] == true),
                    remark = state.remarks[question.questionId]
                )
            }

            val submission = RegistrationSubmission(
                prefix = state.prefix!!.toSubmissionValue(),
                firstName = state.firstName,
                lastName = state.lastName,
                sex = state.sex!!,
                patientType = state.patientType!!,
                documentType = state.documentType,
                idCard = state.idCard.ifBlank { null },
                passportId = state.passportId.ifBlank { null },
                age = state.parsedAge!!,
                telNo = state.telNo,
                underlyingDisease = state.underlyingDisease.ifBlank { null },
                address = state.address.ifBlank { null },
                zipCode = state.zipCode.ifBlank { null },
                shotDate = state.parsedShotDate!!,
                answers = answerList
            )

            submitRegistrationUseCase(submission)
                .onSuccess { confirmation ->
                    _uiState.update { it.copy(isSubmitting = false, isSubmitted = true, successResponse = confirmation) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isSubmitting = false, submitError = error as? DomainError ?: DomainError.NetworkError)
                    }
                }
        }
    }

    fun reset() {
        _uiState.value = ScreeningFormUiState(shotDate = todayDateString())
    }
}
