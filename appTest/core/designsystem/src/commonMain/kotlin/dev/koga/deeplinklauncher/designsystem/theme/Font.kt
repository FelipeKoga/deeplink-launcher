package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.koga.resources.Nunito_Black
import dev.koga.resources.Nunito_Bold
import dev.koga.resources.Nunito_ExtraBold
import dev.koga.resources.Nunito_ExtraLight
import dev.koga.resources.Nunito_Light
import dev.koga.resources.Nunito_Medium
import dev.koga.resources.Nunito_Regular
import dev.koga.resources.Nunito_SemiBold
import dev.koga.resources.Res
import org.jetbrains.compose.resources.Font

val appFont
    @Composable get() = FontFamily(
        Font(Res.font.Nunito_Regular, FontWeight.Normal),
        Font(Res.font.Nunito_Medium, FontWeight.Medium),
        Font(Res.font.Nunito_SemiBold, FontWeight.SemiBold),
        Font(Res.font.Nunito_Bold, FontWeight.Bold),
        Font(Res.font.Nunito_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.Nunito_Black, FontWeight.Black),
        Font(Res.font.Nunito_Light, FontWeight.Light),
        Font(Res.font.Nunito_ExtraLight, FontWeight.ExtraLight),
    )

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
