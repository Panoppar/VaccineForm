package kmch.vaccine.form.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import vaccineform.shared.generated.resources.Res
import vaccineform.shared.generated.resources.notosansthai_variablefont_wdth

@Composable
fun getAppFontFamily(): FontFamily {
    return FontFamily(
        // Note: If using a variable font, these might all render at the same default weight
        // unless you use FontVariation settings or switch to static .ttf files for each weight.
        Font(
            resource = Res.font.notosansthai_variablefont_wdth,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.notosansthai_variablefont_wdth,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.notosansthai_variablefont_wdth,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )
}

@Composable
fun getAppTypography(): Typography {
    val appFontFamily = getAppFontFamily()

    return Typography(
        // ==========================================
        // DISPLAY: Scaled up for maximum impact
        // ==========================================
        displayLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 64.sp,           // Was 57
            lineHeight = 72.sp,         // Was 64
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 52.sp,           // Was 45
            lineHeight = 60.sp,         // Was 52
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 44.sp,           // Was 36
            lineHeight = 52.sp,         // Was 44
            letterSpacing = 0.sp
        ),

        // ==========================================
        // HEADLINES: Scaled up for better section distinction
        // ==========================================
        headlineLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,           // Was 32
            lineHeight = 46.sp,         // Was 40
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 34.sp,           // Was 28
            lineHeight = 42.sp,         // Was 36
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,           // Was 24
            lineHeight = 36.sp,         // Was 32
            letterSpacing = 0.sp
        ),

        // ==========================================
        // TITLES: Increased for form elements and dialogs
        // ==========================================
        titleLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 26.sp,           // Was 22
            lineHeight = 34.sp,         // Was 28
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,           // Was 16
            lineHeight = 28.sp,         // Was 24
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,           // Was 14
            lineHeight = 26.sp,         // Was 20
            letterSpacing = 0.1.sp
        ),

        // ==========================================
        // BODY: The core reading text. Bumping these makes
        // forms and questions much easier to read on mobile.
        // ==========================================
        bodyLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,           // Was 16
            lineHeight = 28.sp,         // Was 24
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,           // Was 14
            lineHeight = 26.sp,         // Was 20
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,           // Was 12
            lineHeight = 24.sp,         // Was 16
            letterSpacing = 0.4.sp
        ),

        // ==========================================
        // LABELS: Adjusted so buttons and small text remain legible
        // ==========================================
        labelLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,           // Was 14
            lineHeight = 24.sp,         // Was 20
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,           // Was 12
            lineHeight = 22.sp,         // Was 16
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,           // Was 11
            lineHeight = 20.sp,         // Was 16
            letterSpacing = 0.5.sp
        )
    )
}