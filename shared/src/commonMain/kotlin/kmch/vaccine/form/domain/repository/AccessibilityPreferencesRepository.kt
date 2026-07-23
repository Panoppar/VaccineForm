package kmch.vaccine.form.domain.repository

import kmch.vaccine.form.domain.model.AccessibilityPreferences

interface AccessibilityPreferencesRepository {
    fun get(): AccessibilityPreferences
    fun set(preferences: AccessibilityPreferences)
}
