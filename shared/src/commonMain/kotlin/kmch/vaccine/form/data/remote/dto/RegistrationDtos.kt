package kmch.vaccine.form.data.remote.dto

import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Sex
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

// ===== POST /api/v1/registrations =====
@Serializable
data class RegistrationAnswerRequestDto(
    val questionId: Int,
    val answer: Boolean, // true = มี/ใช่, false = ไม่มี/ไม่ใช่
    val remark: String? = null
)

@Serializable
data class RegistrationRequestDto(
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val patientType: PatientType,
    val documentType: DocumentType,
    val idCard: String? = null,
    val passportId: String? = null,
    val age: Int,
    val telNo: String,
    val underlyingDisease: String? = null,
    val address: String? = null,
    val zipCode: String? = null,
    val shotDate: LocalDate,
    // เอา vaccineId และ lotId ออกตาม Requirement (Backend ทำ auto FIFO)
    val answers: List<RegistrationAnswerRequestDto>
)

@Serializable
data class RegistrationResponseDto(
    val patientId: Int,
    val screeningRecordId: Int,
    val vaccinationId: Int
)

// ===== GET /api/v1/screening-questions =====
@Serializable
data class ScreeningQuestionDto(
    val questionId: Int,
    val questionText: String,
    val displayOrder: Int
)

// ===== GET /api/v1/registrations =====
@Serializable
data class RegistrationListItemDto(
    val patientId: Int,
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val documentType: DocumentType,
    val idCard: String? = null,
    val passportId: String? = null,
    val telNo: String,
    val patientType: PatientType,
    val shotDate: LocalDate
)

@Serializable
data class RegistrationListResponseDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val items: List<RegistrationListItemDto>
)

// ===== GET /api/v1/registrations/:patient_id =====
@Serializable
data class RegistrationAnswerDetailDto(
    val questionId: Int,
    val questionText: String,
    val answer: Boolean,
    val remark: String? = null
)

@Serializable
data class RegistrationDetailDto(
    val patientId: Int,
    val prefix: String,
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val patientType: PatientType,
    val documentType: DocumentType,
    val idCard: String? = null,
    val passportId: String? = null,
    val age: Int? = null,
    val telNo: String,
    val underlyingDisease: String? = null,
    val address: String? = null,
    val zipCode: String? = null,
    val shotDate: LocalDate,
    val vaccineId: Int? = null,
    val vaccineName: String? = null,
    val lotId: Int? = null,
    val lotNumber: String? = null,
    val answers: List<RegistrationAnswerDetailDto> = emptyList()
)
