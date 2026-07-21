package kmch.vaccine.form.data.repository

import kmch.vaccine.form.config.BuildKonfig
import kmch.vaccine.form.domain.repository.AdminAuthRepository

class AdminAuthRepositoryImpl : AdminAuthRepository {
    override fun authenticate(password: String): Boolean = password == BuildKonfig.ADMIN_PASSWORD
}
