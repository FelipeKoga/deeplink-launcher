package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.koga.resources.Montserrat_Black
import dev.koga.resources.Montserrat_Bold
import dev.koga.resources.Montserrat_Medium
import dev.koga.resources.Montserrat_Regular
import dev.koga.resources.Montserrat_Semibold
import dev.koga.resources.Montserrat_Thin
import dev.koga.resources.Res
import org.jetbrains.compose.resources.Font

val appFont
    @Composable get() = FontFamily(
        Font(Res.font.Montserrat_Thin, FontWeight.Thin),
        Font(Res.font.Montserrat_Regular, FontWeight.Normal),
        Font(Res.font.Montserrat_Medium, FontWeight.Medium),
        Font(Res.font.Montserrat_Semibold, FontWeight.SemiBold),
        Font(Res.font.Montserrat_Bold, FontWeight.Bold),
        Font(Res.font.Montserrat_Black, FontWeight.Black),
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
