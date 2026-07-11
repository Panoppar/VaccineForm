package kmch.vaccine.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.painterResource

import org.jetbrains.compose.resources.Font
import vaccineform.shared.generated.resources.Res
import vaccineform.shared.generated.resources.compose_multiplatform
import vaccineform.shared.generated.resources.notosansthai_variablefont_wdth

@Composable
@Preview
fun App() {
    val thaiFontFamily = FontFamily(
        Font(Res.font.notosansthai_variablefont_wdth)
    )

    val customTypography = Typography().apply {
        // คุณสามารถตั้งค่า copy ให้กับ TextStyles ต่างๆ ให้ใช้ฟอนต์นี้ได้
        // หรือวิธีที่ง่ายที่สุดตอนเริ่มต้นคือการใส่ default font family ไปเลย
    }

    val thaiTypography = Typography(
        displayLarge = Typography().displayLarge.copy(fontFamily = thaiFontFamily),
        displayMedium = Typography().displayMedium.copy(fontFamily = thaiFontFamily),
        displaySmall = Typography().displaySmall.copy(fontFamily = thaiFontFamily),
        headlineLarge = Typography().headlineLarge.copy(fontFamily = thaiFontFamily),
        headlineMedium = Typography().headlineMedium.copy(fontFamily = thaiFontFamily),
        headlineSmall = Typography().headlineSmall.copy(fontFamily = thaiFontFamily),
        titleLarge = Typography().titleLarge.copy(fontFamily = thaiFontFamily),
        titleMedium = Typography().titleMedium.copy(fontFamily = thaiFontFamily),
        titleSmall = Typography().titleSmall.copy(fontFamily = thaiFontFamily),
        bodyLarge = Typography().bodyLarge.copy(fontFamily = thaiFontFamily),
        bodyMedium = Typography().bodyMedium.copy(fontFamily = thaiFontFamily),
        bodySmall = Typography().bodySmall.copy(fontFamily = thaiFontFamily),
        labelLarge = Typography().labelLarge.copy(fontFamily = thaiFontFamily),
        labelMedium = Typography().labelMedium.copy(fontFamily = thaiFontFamily),
        labelSmall = Typography().labelSmall.copy(fontFamily = thaiFontFamily)
    )

    MaterialTheme(
        typography = thaiTypography
    ) {
        VaccineScreeningForm()
    }
}