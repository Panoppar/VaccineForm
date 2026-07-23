package kmch.vaccine.form.presentation.theme

import kotlin.math.pow

// Plain Kotlin, no Compose dependency — the WCAG 2.1 relative-luminance contrast
// formula (https://www.w3.org/TR/WCAG21/#dfn-relative-luminance). Each channel is
// 0.0-1.0. WCAG AA requires >= 4.5 for normal text, >= 3.0 for large text/UI
// components, AAA requires >= 7.0.
fun contrastRatio(r1: Double, g1: Double, b1: Double, r2: Double, g2: Double, b2: Double): Double {
    val luminance1 = relativeLuminance(r1, g1, b1)
    val luminance2 = relativeLuminance(r2, g2, b2)
    val lighter = maxOf(luminance1, luminance2)
    val darker = minOf(luminance1, luminance2)
    return (lighter + 0.05) / (darker + 0.05)
}

private fun relativeLuminance(r: Double, g: Double, b: Double): Double =
    0.2126 * linearize(r) + 0.7152 * linearize(g) + 0.0722 * linearize(b)

private fun linearize(channel: Double): Double =
    if (channel <= 0.03928) channel / 12.92 else ((channel + 0.055) / 1.055).pow(2.4)
