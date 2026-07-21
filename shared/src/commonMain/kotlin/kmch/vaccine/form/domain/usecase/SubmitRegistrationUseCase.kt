package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.RegistrationConfirmation
import kmch.vaccine.form.domain.model.RegistrationSubmission
import kmch.vaccine.form.domain.repository.RegistrationRepository

class SubmitRegistrationUseCase(private val repository: RegistrationRepository) {
    suspend operator fun invoke(submission: RegistrationSubmission): Result<RegistrationConfirmation> =
        repository.submitRegistration(submission)
}
