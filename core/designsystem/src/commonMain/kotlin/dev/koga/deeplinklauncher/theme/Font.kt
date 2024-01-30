package dev.koga.deeplinklauncher.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.icerock.moko.resources.compose.asFont
import dev.koga.resources.MR

val appFont
    @Composable get() = FontFamily(
        listOfNotNull(
            MR.fonts.Montserrat.light.asFont(weight = FontWeight.Light),
            MR.fonts.Montserrat.regular.asFont(weight = FontWeight.Normal),
            MR.fonts.Montserrat.medium.asFont(weight = FontWeight.Medium),
            MR.fonts.Montserrat.semibold.asFont(weight = FontWeight.SemiBold),
            MR.fonts.Montserrat.bold.asFont(weight = FontWeight.Bold),
            MR.fonts.Montserrat.black.asFont(weight = FontWeight.Black),
        ),
    )

// Set of Material typography styles to start with
private val defaultTypography = Typography()

val typography
    @Composable get() = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFont),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFont),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFont),

        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFont),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFont),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFont),

        titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFont),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFont),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFont),

        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFont),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFont),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFont),

        labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFont),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFont),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFont),
    )
