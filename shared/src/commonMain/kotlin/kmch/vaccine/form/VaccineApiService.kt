package kmch.vaccine.form

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ApiException(val statusCode: Int, message: String) : Exception(message)

class VaccineApiService(private val baseUrl: String = API_BASE_URL) {

    suspend fun checkHealth(): HealthStatus =
        httpClient.get("$baseUrl/health").body()

    suspend fun getScreeningQuestions(): List<ScreeningQuestion> =
        httpClient.get("$baseUrl/screening-questions").body()

    // POST /api/v1/registrations
    // Error: 400 (ข้อมูลผิด/เอกสารซ้ำ), 409 (วัคซีนหมดสต็อก)
    suspend fun submitRegistration(request: RegistrationRequest): RegistrationResponse {
        val response = httpClient.post("$baseUrl/registrations") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        return response.body()
    }

    // GET /api/v1/registrations?q=<คำค้น>&page=1&limit=20
    suspend fun listRegistrations(
        query: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): RegistrationListResponse =
        httpClient.get("$baseUrl/registrations") {
            url {
                parameters.append("page", page.toString())
                parameters.append("limit", limit.toString())
                if (!query.isNullOrBlank()) parameters.append("q", query)
            }
        }.body()

    // GET /api/v1/registrations/:patient_id
    suspend fun getRegistrationDetail(patientId: Int): RegistrationDetail? {
        val response = httpClient.get("$baseUrl/registrations/$patientId")
        if (response.status == HttpStatusCode.NotFound) return null
        return response.body()
    }
}
