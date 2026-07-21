package kmch.vaccine.form.domain.repository

import kmch.vaccine.form.domain.model.AppLanguage

interface LanguagePreferenceRepository {
    fun get(): AppLanguage?
    fun set(language: AppLanguage)
}
