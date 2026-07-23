package kmch.vaccine.form.presentation.theme

import androidx.compose.ui.graphics.Color

// ===== Brand palette =====
// This is the single source of truth for the site's colors. Changing the brand
// look going forward should only ever require editing the constants below.
val BrandNavy = Color(0xFF405574)
val BrandTeal = Color(0xFF457492)
val BrandOrange = Color(0xFFFF7800)
val BrandNeutral = Color(0xFFE0E0E0)

// Light Theme Colors
val PrimaryLight = BrandNavy
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD6E0EC)
val OnPrimaryContainerLight = Color(0xFF16233A)

val SecondaryLight = BrandTeal
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFD4E7EE)
val OnSecondaryContainerLight = Color(0xFF102B34)

val TertiaryLight = BrandOrange
// White-on-orange is only 2.65:1 (fails WCAG AA's 4.5:1) — reuse the dark
// brown already defined for the container role instead, which measures
// 5.38:1 (see ColorContrastTest). See presentation/theme/ContrastRatio.kt.
val OnTertiaryLight = Color(0xFF442200)
val TertiaryContainerLight = Color(0xFFFFDFC1)
val OnTertiaryContainerLight = Color(0xFF442200)

val BackgroundLight = Color(0xFFFBFCFE)
val SurfaceLight = Color(0xFFFBFCFE)
val SurfaceVariantLight = BrandNeutral
val OnSurfaceVariantLight = Color(0xFF44474A)
val OutlineLight = Color(0xFF9E9E9E)

// Dark Theme Colors
val PrimaryDark = Color(0xFFAAC7E8)
val OnPrimaryDark = Color(0xFF0F2740)
val PrimaryContainerDark = Color(0xFF2B415C)
val OnPrimaryContainerDark = Color(0xFFD6E0EC)

val SecondaryDark = Color(0xFFA9CBD8)
val OnSecondaryDark = Color(0xFF0A2933)
val SecondaryContainerDark = Color(0xFF2D5866)
val OnSecondaryContainerDark = Color(0xFFD4E7EE)

val TertiaryDark = Color(0xFFFFB874)
val OnTertiaryDark = Color(0xFF4A2800)
val TertiaryContainerDark = Color(0xFF693D00)
val OnTertiaryContainerDark = Color(0xFFFFDFC1)

val BackgroundDark = Color(0xFF10141B)
val SurfaceDark = Color(0xFF10141B)
val SurfaceVariantDark = Color(0xFF44474A)
val OnSurfaceVariantDark = Color(0xFFC5C6C9)
val OutlineDark = Color(0xFF8E9092)

// ===== High contrast variants (AccessibilityPreferences.highContrast) =====
// Maximizes text/background separation (targets AAA ~7:1+ where practical)
// rather than trying to preserve exact brand hues.
val HcPrimaryLight = Color(0xFF14213D)
val HcOnPrimaryLight = Color(0xFFFFFFFF)
val HcSecondaryLight = Color(0xFF0D2E38)
val HcOnSecondaryLight = Color(0xFFFFFFFF)
val HcTertiaryLight = Color(0xFF3D1F00)
val HcOnTertiaryLight = Color(0xFFFFFFFF)
val HcBackgroundLight = Color(0xFFFFFFFF)
val HcOnSurfaceVariantLight = Color(0xFF000000)
val HcOutlineLight = Color(0xFF000000)

val HcPrimaryDark = Color(0xFFB9D4FF)
val HcOnPrimaryDark = Color(0xFF000000)
val HcSecondaryDark = Color(0xFFBEE7F5)
val HcOnSecondaryDark = Color(0xFF000000)
val HcTertiaryDark = Color(0xFFFFD9A8)
val HcOnTertiaryDark = Color(0xFF000000)
val HcBackgroundDark = Color(0xFF000000)
val HcOnSurfaceVariantDark = Color(0xFFFFFFFF)
val HcOutlineDark = Color(0xFFFFFFFF)

// Success (semantic) colors — confirmed/completed states, e.g. vaccination recorded
val SuccessLight = Color(0xFF2E7D32)
val OnSuccessLight = Color(0xFFFFFFFF)
val SuccessContainerLight = Color(0xFFC8E6C9)
val OnSuccessContainerLight = Color(0xFF0B3D0B)

val SuccessDark = Color(0xFF8FD98F)
val OnSuccessDark = Color(0xFF0B3D0B)
val SuccessContainerDark = Color(0xFF1B5E20)
val OnSuccessContainerDark = Color(0xFFC8E6C9)
