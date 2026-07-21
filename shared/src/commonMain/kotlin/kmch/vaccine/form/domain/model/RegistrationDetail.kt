package kmch.vaccine.form.domain.model

import kotlinx.datetime.LocalDate

data class RegistrationAnswerDetail(
    val questionId: Int,
    val questionText: String,
    val answer: Boolean,
    val remark: String? = null
)

data class RegistrationDetail(
    val patientId: Int,
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val patientType: PatientType,
    val documentType: DocumentType,
    val idCard: String?,
    val passportId: String?,
    val age: Int?,
    val telNo: String,
    val underlyingDisease: String?,
    val address: String?,
    val zipCode: String?,
    val shotDate: LocalDate,
    val vaccineId: Int?,
    val vaccineName: String?,
    val lotId: Int?,
    val lotNumber: String?,
    val answers: List<RegistrationAnswerDetail> = emptyList()
)
