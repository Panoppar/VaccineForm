package kmch.vaccine.form

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ===== Enums (ค่า JSON เป็น snake_case ตาม Backend Golang) =====

@Serializable
enum class Sex {
    @SerialName("male") MALE,
    @SerialName("female") FEMALE
}

@Serializable
enum class PatientType {
    @SerialName("student") STUDENT,
    @SerialName("employee") EMPLOYEE,
    @SerialName("company") COMPANY,
    @SerialName("external") EXTERNAL
}

@Serializable
enum class DocumentType {
    @SerialName("id_card") ID_CARD,
    @SerialName("passport") PASSPORT
}

// ===== POST /api/v1/registrations =====
@Serializable
data class RegistrationAnswerRequest(
    val questionId: Int,
    val answer: Boolean, // true = มี/ใช่, false = ไม่มี/ไม่ใช่
    val remark: String? = null
)

@Serializable
data class RegistrationRequest(
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
    val answers: List<RegistrationAnswerRequest>
)

@Serializable
data class RegistrationResponse(
    val patientId: Int,
    val screeningRecordId: Int,
    val vaccinationId: Int
)

// ===== Vaccine & Lot Management (สำหรับ Admin) =====

@Serializable
data class VaccineCreateRequest(
    val vaccineName: String
)

@Serializable
data class VaccineCreateResponse(
    val vaccineId: Int
)

@Serializable
data class LotCreateRequest(
    val lotNumber: String,
    val initialQuantity: Int
)

@Serializable
data class LotCreateResponse(
    val lotId: Int
)

// ===== Direct Vaccination Record =====

@Serializable
data class VaccinationRecordRequest(
    val patientId: Int,
    val lotId: Int, // ต้องระบุเจาะจงเสมอ
    val shotDate: LocalDate
)

// ===== GET /api/v1/screening-questions =====

@Serializable
data class ScreeningQuestion(
    val questionId: Int,
    val questionText: String,
    val displayOrder: Int
)

// ===== GET /api/v1/registrations =====

@Serializable
data class RegistrationListItem(
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
data class RegistrationListResponse(
    val total: Int,
    val page: Int,
    val limit: Int,
    val items: List<RegistrationListItem>
)

// ===== GET /api/v1/registrations/:patient_id =====

@Serializable
data class RegistrationAnswerDetail(
    val questionId: Int,
    val questionText: String,
    val answer: Boolean,
    val remark: String? = null
)

@Serializable
data class RegistrationDetail(
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
    val answers: List<RegistrationAnswerDetail> = emptyList()
)

// ===== GET /health, /api/v1/health =====

@Serializable
data class HealthStatus(
    val status: String
)
