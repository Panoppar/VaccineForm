package kmch.vaccine.form.domain.model

import kotlinx.datetime.LocalDate

data class RegistrationSubmission(
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val patientType: PatientType,
    val documentType: DocumentType,
    val idCard: String?,
    val passportId: String?,
    val age: Int,
    val telNo: String,
    val underlyingDisease: String?,
    val address: String?,
    val zipCode: String?,
    val shotDate: LocalDate,
    val answers: List<ScreeningAnswer>
)

data class RegistrationConfirmation(
    val patientId: Int,
    val screeningRecordId: Int,
    val vaccinationId: Int
)
