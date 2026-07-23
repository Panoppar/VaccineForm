package kmch.vaccine.form.presentation.theme

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertTrue

// Guards WCAG 2.1 AA text contrast (>= 4.5:1) for every "on-top-of" color pair
// this app actually renders text/icons with. This is the test that would have
// caught OnTertiaryLight being white-on-orange (2.65:1) before it shipped —
// extend this list whenever a new color pair is added to Color.kt.
class ColorContrastTest {

    private data class Pair(val label: String, val foreground: Color, val background: Color)

    private val minimumAaRatio = 4.5

    private val pairs = listOf(
        // Light scheme
        Pair("Light: onPrimary / primary", OnPrimaryLight, PrimaryLight),
        Pair("Light: onPrimaryContainer / primaryContainer", OnPrimaryContainerLight, PrimaryContainerLight),
        Pair("Light: onSecondary / secondary", OnSecondaryLight, SecondaryLight),
        Pair("Light: onSecondaryContainer / secondaryContainer", OnSecondaryContainerLight, SecondaryContainerLight),
        Pair("Light: onTertiary / tertiary", OnTertiaryLight, TertiaryLight),
        Pair("Light: onTertiaryContainer / tertiaryContainer", OnTertiaryContainerLight, TertiaryContainerLight),
        Pair("Light: onSuccess / success", OnSuccessLight, SuccessLight),
        Pair("Light: onSuccessContainer / successContainer", OnSuccessContainerLight, SuccessContainerLight),

        // Dark scheme
        Pair("Dark: onPrimary / primary", OnPrimaryDark, PrimaryDark),
        Pair("Dark: onPrimaryContainer / primaryContainer", OnPrimaryContainerDark, PrimaryContainerDark),
        Pair("Dark: onSecondary / secondary", OnSecondaryDark, SecondaryDark),
        Pair("Dark: onSecondaryContainer / secondaryContainer", OnSecondaryContainerDark, SecondaryContainerDark),
        Pair("Dark: onTertiary / tertiary", OnTertiaryDark, TertiaryDark),
        Pair("Dark: onTertiaryContainer / tertiaryContainer", OnTertiaryContainerDark, TertiaryContainerDark),
        Pair("Dark: onSuccess / success", OnSuccessDark, SuccessDark),
        Pair("Dark: onSuccessContainer / successContainer", OnSuccessContainerDark, SuccessContainerDark),

        // High contrast light
        Pair("HC Light: onPrimary / primary", HcOnPrimaryLight, HcPrimaryLight),
        Pair("HC Light: onSecondary / secondary", HcOnSecondaryLight, HcSecondaryLight),
        Pair("HC Light: onTertiary / tertiary", HcOnTertiaryLight, HcTertiaryLight),

        // High contrast dark
        Pair("HC Dark: onPrimary / primary", HcOnPrimaryDark, HcPrimaryDark),
        Pair("HC Dark: onSecondary / secondary", HcOnSecondaryDark, HcSecondaryDark),
        Pair("HC Dark: onTertiary / tertiary", HcOnTertiaryDark, HcTertiaryDark)
    )

    @Test
    fun everyTextColorPairMeetsWcagAa() {
        for (pair in pairs) {
            val ratio = contrastRatio(
                pair.foreground.red.toDouble(), pair.foreground.green.toDouble(), pair.foreground.blue.toDouble(),
                pair.background.red.toDouble(), pair.background.green.toDouble(), pair.background.blue.toDouble()
            )
            assertTrue(
                ratio >= minimumAaRatio,
                "${pair.label} is only ${(ratio * 100).toInt() / 100.0}:1, needs >= $minimumAaRatio:1 for WCAG AA"
            )
        }
    }
}
