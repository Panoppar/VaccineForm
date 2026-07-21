package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.RegistrationPage
import kmch.vaccine.form.domain.repository.RegistrationRepository

class ListRegistrationsUseCase(private val repository: RegistrationRepository) {
    suspend operator fun invoke(query: String? = null, page: Int = 1, limit: Int = 20): Result<RegistrationPage> =
        repository.listRegistrations(query, page, limit)
}
