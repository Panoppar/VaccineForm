package kmch.vaccine.form.presentation.accessibility

import kmch.vaccine.form.domain.model.AccessibilityPreferences
import kmch.vaccine.form.domain.repository.AccessibilityPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// App-scoped (Koin single) holder for accessibility settings, same shape as
// LocalizationController. Persists so a page refresh keeps the user's choice.
// There is no in-app UI to change these yet (the first-visit config wizard is
// a separate future feature) — this is the groundwork it will plug into.
class AccessibilityController(
    private val accessibilityPreferencesRepository: AccessibilityPreferencesRepository
) {
    private val _preferences = MutableStateFlow(accessibilityPreferencesRepository.get())
    val preferences: StateFlow<AccessibilityPreferences> = _preferences.asStateFlow()

    fun setFontScale(fontScale: Float) = update { it.copy(fontScale = fontScale) }
    fun setHighContrast(enabled: Boolean) = update { it.copy(highContrast = enabled) }
    fun setReduceMotion(enabled: Boolean) = update { it.copy(reduceMotion = enabled) }

    private fun update(transform: (AccessibilityPreferences) -> AccessibilityPreferences) {
        val next = transform(_preferences.value)
        _preferences.value = next
        accessibilityPreferencesRepository.set(next)
    }
}
