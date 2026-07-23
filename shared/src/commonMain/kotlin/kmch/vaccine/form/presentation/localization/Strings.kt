package kmch.vaccine.form.presentation.localization

import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.Sex

// Single source of truth for every user-facing string in the app. Add a field
// here, then implement it in both StringsTh and StringsEn — the compiler will
// flag anything missing since both objects must implement this interface.
interface Strings {

    // ===== Common =====
    val ok: String
    val cancel: String
    val save: String
    val notice: String

    // ===== Navigation / 404 =====
    val notFoundTitle: String
    val notFoundMessage: String
    val goToScreeningForm: String

    // ===== Screening form: loading / errors =====
    val screeningLoadError: String
    val retry: String

    // ===== Screening form: header =====
    val screeningFormTitle: String
    val screeningFormSubtitle: String

    // ===== Screening form: patient info section =====
    val patientInfoSectionTitle: String
    val prefixLabel: String
    fun prefixOptionLabel(prefix: Prefix): String
    val firstNameLabel: String
    val lastNameLabel: String
    val sexLabel: String
    fun sexOptionLabel(sex: Sex): String
    val patientTypeLabel: String
    val patientTypeSelectLabel: String
    fun patientTypeOptionLabel(type: PatientType): String
    val documentTypeLabel: String
    fun documentTypeOptionLabel(type: DocumentType): String
    val idCardNumberLabel: String
    fun idCardDigitError(current: Int): String
    val passportNumberLabel: String
    val ageLabel: String
    val phoneNumberLabel: String
    fun phoneDigitError(current: Int): String
    val vaccinationDateLabel: String
    val selectDateContentDescription: String
    val underlyingDiseaseLabel: String
    val addressLabel: String
    val zipCodeLabel: String

    // ===== Screening form: questions section =====
    val healthScreeningSectionTitle: String
    val noScreeningQuestionsMessage: String
    val specifyYourAnswerLabel: String
    val answerYes: String
    val answerNo: String

    // ===== Screening form: consent section =====
    val pdpaConsentText: String
    val pdpaLinkLabel: String
    val vaccineInfoConsentText: String

    // ===== Screening form: submission =====
    val submitErrorInvalidOrDuplicate: String
    val submitErrorVaccineOutOfStock: String
    fun submitErrorGeneric(code: Int): String
    val submitErrorNetwork: String
    val submitting: String
    val confirmAndSubmit: String

    // ===== Screening form: success screen =====
    val submissionSuccessTitle: String
    val patientIdLabel: String
    val vaccinationIdLabel: String
    val submissionSuccessNote: String
    val returnToHome: String

    // ===== Admin: dashboard/list =====
    val adminDashboardTitle: String
    val lotIdLabel: String
    val addVaccineOrLotContentDescription: String
    val employeeRateIdLabel: String
    val searchPatientsLabel: String
    val createNewScreeningContentDescription: String
    fun noPatientsFoundMessage(query: String): String
    fun patientListIdLine(id: Int): String
    fun patientListDateLine(date: String): String
    fun patientListNameLine(name: String): String
    fun patientListPhoneLine(tel: String): String

    // ===== Admin: detail =====
    val backButton: String
    val newScreeningByStaffTitle: String
    fun screeningDetailTitle(patientId: Int?): String
    val patientNotFoundMessage: String
    val answerHistoryTitle: String
    val printScreeningFormButton: String
    fun patientHeaderName(name: String): String
    fun patientHeaderId(id: Int): String
    fun patientHeaderPhone(tel: String): String
    fun patientHeaderUnderlyingDisease(disease: String): String
    fun vaccinationDateChip(date: String, vaccineName: String?, lotNumber: String?): String
    fun answerRemark(remark: String): String

    // ===== Admin: add vaccine/lot dialog =====
    val manageVaccineDataTitle: String
    val addNewVaccineOption: String
    val addNewLotOption: String
    val vaccineNameLabel: String
    val vaccineIdRefLabel: String
    val lotNumberLabel: String
    val initialQuantityLabel: String
    fun vaccineCreatedMessage(vaccineId: Int): String
    fun lotCreatedMessage(lotId: Int): String
    fun genericErrorMessage(message: String): String
    val vaccineIdMustBeNumberError: String

    // ===== Admin: login =====
    val adminLoginTitle: String
    val adminLoginSubtitle: String
    val passwordLabel: String
    val passwordIncorrectError: String
    val passwordLockedOutError: String
    val backToHome: String
    val logIn: String
}
