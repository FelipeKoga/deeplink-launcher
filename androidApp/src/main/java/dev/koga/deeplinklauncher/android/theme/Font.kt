package dev.koga.deeplinklauncher.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.koga.deeplinklauncher.android.R

val AppFont = FontFamily(
    listOf(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
        Font(R.font.montserrat_bold, FontWeight.Bold),
    )
)
// Set of Material typography styles to start with
private val defaultTypography = Typography()

val typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont)
)