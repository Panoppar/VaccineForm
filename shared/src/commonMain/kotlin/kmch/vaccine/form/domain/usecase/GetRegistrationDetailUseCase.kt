package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.repository.RegistrationRepository

class GetRegistrationDetailUseCase(private val repository: RegistrationRepository) {
    suspend operator fun invoke(patientId: Int): Result<RegistrationDetail?> =
        repository.getRegistrationDetail(patientId)
}
