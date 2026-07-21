package kmch.vaccine.form.data.remote.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

// ===== Vaccine & Lot Management (สำหรับ Admin) =====
@Serializable
data class VaccineCreateRequestDto(
    val vaccineName: String
)

@Serializable
data class VaccineCreateResponseDto(
    val vaccineId: Int
)

@Serializable
data class LotCreateRequestDto(
    val lotNumber: String,
    val initialQuantity: Int
)

@Serializable
data class LotCreateResponseDto(
    val lotId: Int
)

// ===== Direct Vaccination Record =====
@Serializable
data class VaccinationRecordRequestDto(
    val patientId: Int,
    val lotId: Int, // ต้องระบุเจาะจงเสมอ
    val shotDate: LocalDate
)

// ===== GET /health, /api/v1/health =====
@Serializable
data class HealthStatusDto(
    val status: String
)
