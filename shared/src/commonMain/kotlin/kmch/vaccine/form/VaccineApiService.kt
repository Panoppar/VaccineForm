package kmch.vaccine.form

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CancellationException

class VaccineApiService {
    private val baseUrl = "https://mobile.kmch.kmitl.ac.th/api/v1" // เปลี่ยนตามจริง

    // --- GET ---
    suspend fun getPatients(): List<Patient> =
        httpClient.get("$baseUrl/patients").body()

    suspend fun getQuestions(): List<ScreeningQuestion> =
        httpClient.get("$baseUrl/screening-questions").body()

    suspend fun getPatientRecords(patientId: Int): List<ScreeningRecord> =
        httpClient.get("$baseUrl/patients/$patientId/records").body()

    suspend fun getRecordAnswers(recordId: Int): List<ScreeningAnswer> =
        httpClient.get("$baseUrl/records/$recordId/answers").body()

    // --- POST ---
    suspend fun submitScreeningForm(data: ScreeningSubmissionRequest): Boolean {
        return try {
            val response = httpClient.post("$baseUrl/screening") {
                contentType(ContentType.Application.Json)
                setBody(data) // Ktor จะแปลง data เป็น JSON อัตโนมัติ (ต้องมี @Serializable)
            }
            // คืนค่า true หาก HTTP Status Code อยู่ในช่วง 200-299
            response.status.isSuccess()
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            false
        }
    }

    suspend fun confirmVaccination(recordData: VaccinationRecord): Boolean {
        val response = httpClient.post("$baseUrl/vaccinations") {
            contentType(ContentType.Application.Json)
            setBody(recordData)
        }
        return response.status.isSuccess()
    }
}