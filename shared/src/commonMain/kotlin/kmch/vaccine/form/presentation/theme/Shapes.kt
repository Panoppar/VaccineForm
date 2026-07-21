package kmch.vaccine.form.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Centralizes the corner radii used across cards, chips, and dialogs so the
// site's overall roundedness can be tuned from one place.
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// Used for the small status/info chips (e.g. "confirmed" badge, vaccination date pill).
val ChipShape = RoundedCornerShape(8.dp)
