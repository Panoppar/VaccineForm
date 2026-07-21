package kmch.vaccine.form.domain.repository

interface AdminAuthRepository {
    fun authenticate(password: String): Boolean
}
