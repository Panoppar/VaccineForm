package kmch.vaccine.form.presentation.localization

import kmch.vaccine.form.domain.model.AppLanguage
import kmch.vaccine.form.domain.repository.LanguagePreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// App-scoped (Koin single) holder for the active language. Persists the choice
// so a page refresh keeps whatever the user last picked.
class LocalizationController(
    private val languagePreferenceRepository: LanguagePreferenceRepository
) {
    private val _language = MutableStateFlow(languagePreferenceRepository.get() ?: AppLanguage.TH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _language.value = language
        languagePreferenceRepository.set(language)
    }
}

fun strings(language: AppLanguage): Strings = when (language) {
    AppLanguage.TH -> StringsTh
    AppLanguage.EN -> StringsEn
}
