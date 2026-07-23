package kmch.vaccine.form.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Material3's ColorScheme has no built-in "success" slot, so this mirrors it as a
// small side-car for confirmed/completed states (e.g. vaccination recorded).
data class SuccessColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color
)

@Composable
fun successColors(darkTheme: Boolean = isSystemInDarkTheme()): SuccessColors =
    if (darkTheme) {
        SuccessColors(SuccessDark, OnSuccessDark, SuccessContainerDark, OnSuccessContainerDark)
    } else {
        SuccessColors(SuccessLight, OnSuccessLight, SuccessContainerLight, OnSuccessContainerLight)
    }

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = HcPrimaryLight,
    onPrimary = HcOnPrimaryLight,
    primaryContainer = HcPrimaryLight,
    onPrimaryContainer = HcOnPrimaryLight,
    secondary = HcSecondaryLight,
    onSecondary = HcOnSecondaryLight,
    secondaryContainer = HcSecondaryLight,
    onSecondaryContainer = HcOnSecondaryLight,
    tertiary = HcTertiaryLight,
    onTertiary = HcOnTertiaryLight,
    tertiaryContainer = HcTertiaryLight,
    onTertiaryContainer = HcOnTertiaryLight,
    background = HcBackgroundLight,
    surface = HcBackgroundLight,
    surfaceVariant = HcBackgroundLight,
    onSurfaceVariant = HcOnSurfaceVariantLight,
    outline = HcOutlineLight
)

private val HighContrastDarkColorScheme = darkColorScheme(
    primary = HcPrimaryDark,
    onPrimary = HcOnPrimaryDark,
    primaryContainer = HcPrimaryDark,
    onPrimaryContainer = HcOnPrimaryDark,
    secondary = HcSecondaryDark,
    onSecondary = HcOnSecondaryDark,
    secondaryContainer = HcSecondaryDark,
    onSecondaryContainer = HcOnSecondaryDark,
    tertiary = HcTertiaryDark,
    onTertiary = HcOnTertiaryDark,
    tertiaryContainer = HcTertiaryDark,
    onTertiaryContainer = HcOnTertiaryDark,
    background = HcBackgroundDark,
    surface = HcBackgroundDark,
    surfaceVariant = HcBackgroundDark,
    onSurfaceVariant = HcOnSurfaceVariantDark,
    outline = HcOutlineDark
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        if (highContrast) HighContrastDarkColorScheme else DarkColorScheme
    } else {
        if (highContrast) HighContrastLightColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getAppTypography(),
        shapes = AppShapes,
        content = content
    )
}
