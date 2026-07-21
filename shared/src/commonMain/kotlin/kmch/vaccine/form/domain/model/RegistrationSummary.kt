package kmch.vaccine.form.domain.model

import kotlinx.datetime.LocalDate

data class RegistrationSummary(
    val patientId: Int,
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val documentType: DocumentType,
    val idCard: String?,
    val passportId: String?,
    val telNo: String,
    val patientType: PatientType,
    val shotDate: LocalDate
)

data class RegistrationPage(
    val total: Int,
    val page: Int,
    val limit: Int,
    val items: List<RegistrationSummary>
)
