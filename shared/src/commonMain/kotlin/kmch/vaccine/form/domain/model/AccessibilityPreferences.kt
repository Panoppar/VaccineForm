package kmch.vaccine.form.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessibilityPreferences(
    val fontScale: Float = 1f,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false
)
