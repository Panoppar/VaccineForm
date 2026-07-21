package kmch.vaccine.form.domain.repository

import kmch.vaccine.form.domain.model.RegistrationConfirmation
import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.model.RegistrationPage
import kmch.vaccine.form.domain.model.RegistrationSubmission
import kmch.vaccine.form.domain.model.ScreeningQuestion

interface RegistrationRepository {
    suspend fun getScreeningQuestions(): Result<List<ScreeningQuestion>>
    suspend fun submitRegistration(submission: RegistrationSubmission): Result<RegistrationConfirmation>
    suspend fun listRegistrations(query: String?, page: Int = 1, limit: Int = 20): Result<RegistrationPage>
    suspend fun getRegistrationDetail(patientId: Int): Result<RegistrationDetail?>
}
