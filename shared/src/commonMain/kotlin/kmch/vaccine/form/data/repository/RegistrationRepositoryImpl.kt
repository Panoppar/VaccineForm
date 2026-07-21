package kmch.vaccine.form.data.repository

import kmch.vaccine.form.data.mapper.toDomain
import kmch.vaccine.form.data.mapper.toRequestDto
import kmch.vaccine.form.data.remote.VaccineApiService
import kmch.vaccine.form.data.remote.runCatchingDomain
import kmch.vaccine.form.domain.model.RegistrationConfirmation
import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.model.RegistrationPage
import kmch.vaccine.form.domain.model.RegistrationSubmission
import kmch.vaccine.form.domain.model.ScreeningQuestion
import kmch.vaccine.form.domain.repository.RegistrationRepository

class RegistrationRepositoryImpl(
    private val api: VaccineApiService
) : RegistrationRepository {

    override suspend fun getScreeningQuestions(): Result<List<ScreeningQuestion>> = runCatchingDomain {
        api.getScreeningQuestions().map { it.toDomain() }
    }

    override suspend fun submitRegistration(submission: RegistrationSubmission): Result<RegistrationConfirmation> =
        runCatchingDomain {
            api.submitRegistration(submission.toRequestDto()).toDomain()
        }

    override suspend fun listRegistrations(query: String?, page: Int, limit: Int): Result<RegistrationPage> =
        runCatchingDomain {
            api.listRegistrations(query, page, limit).toDomain()
        }

    override suspend fun getRegistrationDetail(patientId: Int): Result<RegistrationDetail?> = runCatchingDomain {
        api.getRegistrationDetail(patientId)?.toDomain()
    }
}
