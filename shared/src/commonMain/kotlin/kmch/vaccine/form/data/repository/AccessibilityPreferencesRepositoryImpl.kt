package kmch.vaccine.form.data.repository

import kmch.vaccine.form.domain.model.AccessibilityPreferences
import kmch.vaccine.form.domain.repository.AccessibilityPreferencesRepository
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val ACCESSIBILITY_STORAGE_KEY = "kmch_vaccine_form_accessibility"

class AccessibilityPreferencesRepositoryImpl : AccessibilityPreferencesRepository {

    override fun get(): AccessibilityPreferences =
        runCatching { window.localStorage.getItem(ACCESSIBILITY_STORAGE_KEY) }
            .getOrNull()
            ?.let { stored -> runCatching { Json.decodeFromString<AccessibilityPreferences>(stored) }.getOrNull() }
            ?: AccessibilityPreferences()

    override fun set(preferences: AccessibilityPreferences) {
        runCatching {
            window.localStorage.setItem(ACCESSIBILITY_STORAGE_KEY, Json.encodeToString(preferences))
        }
    }
}
