package kmch.vaccine.form.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kmch.vaccine.form.data.remote.dto.HealthStatusDto
import kmch.vaccine.form.data.remote.dto.LotCreateRequestDto
import kmch.vaccine.form.data.remote.dto.LotCreateResponseDto
import kmch.vaccine.form.data.remote.dto.RegistrationDetailDto
import kmch.vaccine.form.data.remote.dto.RegistrationListResponseDto
import kmch.vaccine.form.data.remote.dto.RegistrationRequestDto
import kmch.vaccine.form.data.remote.dto.RegistrationResponseDto
import kmch.vaccine.form.data.remote.dto.ScreeningQuestionDto
import kmch.vaccine.form.data.remote.dto.VaccinationRecordRequestDto
import kmch.vaccine.form.data.remote.dto.VaccineCreateRequestDto
import kmch.vaccine.form.data.remote.dto.VaccineCreateResponseDto

class ApiException(val statusCode: Int, message: String) : Exception(message)

class VaccineApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String = API_BASE_URL
) {

    suspend fun checkHealth(): HealthStatusDto =
        httpClient.get("$baseUrl/health").body()

    suspend fun getScreeningQuestions(): List<ScreeningQuestionDto> =
        httpClient.get("$baseUrl/screening-questions").body()

    // POST /api/v1/registrations
    // Backend จะทำการ Auto-FIFO เลือกล็อตให้เอง
    suspend fun submitRegistration(request: RegistrationRequestDto): RegistrationResponseDto {
        val response = httpClient.post("$baseUrl/registrations") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        return response.body()
    }

    // GET /api/v1/registrations
    suspend fun listRegistrations(
        query: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): RegistrationListResponseDto =
        httpClient.get("$baseUrl/registrations") {
            url {
                parameters.append("page", page.toString())
                parameters.append("limit", limit.toString())
                if (!query.isNullOrBlank()) parameters.append("q", query)
            }
        }.body()

    // GET /api/v1/registrations/:patient_id
    suspend fun getRegistrationDetail(patientId: Int): RegistrationDetailDto? {
        val response = httpClient.get("$baseUrl/registrations/$patientId")
        if (response.status == HttpStatusCode.NotFound) return null
        return response.body()
    }

    // ==========================================
    // Vaccine & Lot Management API
    // ==========================================

    // POST /api/v1/vaccines
    suspend fun createVaccine(request: VaccineCreateRequestDto): VaccineCreateResponseDto {
        val response = httpClient.post("$baseUrl/vaccines") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        return response.body()
    }

    // POST /api/v1/vaccines/:vaccine_id/lots
    suspend fun createLot(vaccineId: Int, request: LotCreateRequestDto): LotCreateResponseDto {
        val response = httpClient.post("$baseUrl/vaccines/$vaccineId/lots") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        return response.body()
    }

    // ==========================================
    // Direct Vaccination Record API
    // ==========================================

    // POST /api/v1/vaccination-records
    // บันทึกตรง หักสต็อกทันที (ไม่มี Auto-FIFO) อาจเจอปัญหา 404 (ไม่มีผู้ป่วย), 409 (วัคซีนหมด)
    suspend fun createVaccinationRecord(request: VaccinationRecordRequestDto) {
        val response = httpClient.post("$baseUrl/vaccination-records") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        // ไม่มีการ Return Body พิเศษ หรือถ้ามีสามารถเพิ่ม Response Data Class มารับได้เลย
    }
}
