package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Semantic typography tokens for DeepLink Launcher.
 *
 * Mapping from legacy Material3 usage:
 * - titleLarge + Bold → title.sheet
 * - titleSmall + Bold → title.dialog
 * - titleMedium + Bold → title.topBar
 * - titleMedium + SemiBold → title.card
 * - headlineMedium + Bold → title.page
 * - labelLarge + SemiBold → label.section / action.button
 * - labelSmall + Bold → label.fieldHeader
 * - labelMedium + SemiBold → label.field
 * - labelMedium + Bold → label.error
 * - labelSmall + SemiBold → label.chip
 * - labelSmall + Medium → label.badge
 * - labelSmall (plain) → label.caption
 * - bodyLarge + SemiBold → action.tab
 * - bodyMedium → body.default
 * - bodyMedium + SemiBold → body.emphasis
 * - bodySmall → body.small
 * - bodySmall + Medium → body.smallEmphasis
 * - bodyMedium + Monospace → code.link
 * - 14sp Monospace → code.block
 * - labelLarge 16sp SemiBold → action.dropdown
 */
@Immutable
data class TitleTypography(
    val sheet: TextStyle,
    val dialog: TextStyle,
    val topBar: TextStyle,
    val card: TextStyle,
    val page: TextStyle,
)

@Immutable
data class BodyTypography(
    val default: TextStyle,
    val small: TextStyle,
    val smallEmphasis: TextStyle,
    val emphasis: TextStyle,
)

@Immutable
data class LabelTypography(
    val section: TextStyle,
    val fieldHeader: TextStyle,
    val field: TextStyle,
    val chip: TextStyle,
    val caption: TextStyle,
    val badge: TextStyle,
    val error: TextStyle,
)

@Immutable
data class ActionTypography(
    val button: TextStyle,
    val tab: TextStyle,
    val dropdown: TextStyle,
)

@Immutable
data class CodeTypography(
    val link: TextStyle,
    val block: TextStyle,
)

@Immutable
data class DeepLinkTypography(
    val title: TitleTypography,
    val body: BodyTypography,
    val label: LabelTypography,
    val action: ActionTypography,
    val code: CodeTypography,
)

@Composable
internal fun rememberDeepLinkTypography(): DeepLinkTypography {
    val material = materialTypography()
    val monospace = FontFamily.Monospace

    return remember(material) {
        DeepLinkTypography(
            title = TitleTypography(
                sheet = material.titleLarge.copy(fontWeight = FontWeight.Bold),
                dialog = material.titleSmall.copy(fontWeight = FontWeight.Bold),
                topBar = material.titleMedium.copy(fontWeight = FontWeight.Bold),
                card = material.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                page = material.headlineMedium.copy(fontWeight = FontWeight.Bold),
            ),
            body = BodyTypography(
                default = material.bodyMedium,
                small = material.bodySmall,
                smallEmphasis = material.bodySmall.copy(fontWeight = FontWeight.Medium),
                emphasis = material.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            ),
            label = LabelTypography(
                section = material.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                fieldHeader = material.labelSmall.copy(fontWeight = FontWeight.Bold),
                field = material.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                chip = material.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                caption = material.labelSmall,
                badge = material.labelSmall.copy(fontWeight = FontWeight.Medium),
                error = material.labelMedium.copy(fontWeight = FontWeight.Bold),
            ),
            action = ActionTypography(
                button = material.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                tab = material.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                dropdown = material.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                ),
            ),
            code = CodeTypography(
                link = material.bodyMedium.copy(fontFamily = monospace),
                block = TextStyle(
                    fontFamily = monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
        )
    }
}

internal fun DeepLinkTypography.toMaterialTypography(): Typography {
    return Typography(
        displayLarge = title.page,
        displayMedium = title.page,
        displaySmall = title.sheet,

        headlineLarge = title.page,
        headlineMedium = title.page,
        headlineSmall = title.dialog,

        titleLarge = title.sheet,
        titleMedium = title.topBar,
        titleSmall = title.dialog,

        bodyLarge = action.tab,
        bodyMedium = body.default,
        bodySmall = body.small,

        labelLarge = action.button,
        labelMedium = label.field,
        labelSmall = label.caption,
    )
}
