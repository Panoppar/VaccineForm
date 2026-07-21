package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.repository.AdminAuthRepository

class AuthenticateAdminUseCase(private val repository: AdminAuthRepository) {
    operator fun invoke(password: String): Boolean = repository.authenticate(password)
}
