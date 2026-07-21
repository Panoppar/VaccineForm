package kmch.vaccine.form.data.repository

import kmch.vaccine.form.domain.model.AppLanguage
import kmch.vaccine.form.domain.repository.LanguagePreferenceRepository
import kotlinx.browser.window

private const val LANGUAGE_STORAGE_KEY = "kmch_vaccine_form_language"

class LanguagePreferenceRepositoryImpl : LanguagePreferenceRepository {

    override fun get(): AppLanguage? =
        runCatching { window.localStorage.getItem(LANGUAGE_STORAGE_KEY) }
            .getOrNull()
            ?.let { stored -> AppLanguage.entries.find { it.name == stored } }

    override fun set(language: AppLanguage) {
        runCatching { window.localStorage.setItem(LANGUAGE_STORAGE_KEY, language.name) }
    }
}
