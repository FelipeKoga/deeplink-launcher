package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@PreviewLightDark
@Composable
private fun DeepLinkColorsCorePreview() {
    DLLPreviewTheme {
        val colors = DeepLinkTheme.colors

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ColorGroup(
                name = "text",
                tokens = listOf(
                    "primary" to colors.text.primary,
                    "secondary" to colors.text.secondary,
                    "muted" to colors.text.muted,
                    "placeholder" to colors.text.placeholder,
                    "inverse" to colors.text.inverse,
                    "error" to colors.text.error,
                ),
            )

            ColorGroup(
                name = "surface",
                tokens = listOf(
                    "background" to colors.surface.background,
                    "card" to colors.surface.card,
                    "elevated" to colors.surface.elevated,
                    "muted" to colors.surface.muted,
                    "primary" to colors.surface.primary,
                ),
            )

            ColorGroup(
                name = "border",
                tokens = listOf(
                    "subtle" to colors.border.subtle,
                    "default" to colors.border.default,
                    "strong" to colors.border.strong,
                ),
            )

            ColorGroup(
                name = "button",
                tokens = listOf(
                    "primaryBackground" to colors.button.primaryBackground,
                    "primaryContent" to colors.button.primaryContent,
                    "secondaryBackground" to colors.button.secondaryBackground,
                    "secondaryContent" to colors.button.secondaryContent,
                    "textContent" to colors.button.textContent,
                    "textDestructiveContent" to colors.button.textDestructiveContent,
                    "destructiveBackground" to colors.button.destructiveBackground,
                    "destructiveContent" to colors.button.destructiveContent,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkColorsSemanticPreview() {
    DLLPreviewTheme {
        val colors = DeepLinkTheme.colors

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ColorGroup(
                name = "category",
                tokens = listOf(
                    "webLink.background" to colors.category.webLink.background,
                    "webLink.content" to colors.category.webLink.content,
                    "phone.background" to colors.category.phone.background,
                    "phone.content" to colors.category.phone.content,
                    "sms.background" to colors.category.sms.background,
                    "sms.content" to colors.category.sms.content,
                    "location.background" to colors.category.location.background,
                    "location.content" to colors.category.location.content,
                    "androidIntent.background" to colors.category.androidIntent.background,
                    "androidIntent.content" to colors.category.androidIntent.content,
                    "system.background" to colors.category.system.background,
                    "system.content" to colors.category.system.content,
                    "customScheme.background" to colors.category.customScheme.background,
                    "customScheme.content" to colors.category.customScheme.content,
                ),
            )

            ColorGroup(
                name = "status",
                tokens = listOf(
                    "errorBackground" to colors.status.errorBackground,
                    "errorContent" to colors.status.errorContent,
                    "successBackground" to colors.status.successBackground,
                    "successContent" to colors.status.successContent,
                ),
            )

            ColorGroup(
                name = "accent",
                tokens = listOf(
                    "favorite" to colors.accent.favorite,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkTypographyStylesPreview() {
    DLLPreviewTheme {
        val colors = DeepLinkTheme.colors
        val typography = DeepLinkTheme.typography

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            listOf(
                "title.sheet" to typography.title.sheet,
                "title.dialog" to typography.title.dialog,
                "title.topBar" to typography.title.topBar,
                "title.card" to typography.title.card,
                "title.page" to typography.title.page,
                "body.default" to typography.body.default,
                "body.small" to typography.body.small,
                "body.smallEmphasis" to typography.body.smallEmphasis,
                "body.emphasis" to typography.body.emphasis,
                "label.section" to typography.label.section,
                "label.fieldHeader" to typography.label.fieldHeader,
                "label.field" to typography.label.field,
                "label.chip" to typography.label.chip,
                "label.caption" to typography.label.caption,
                "label.badge" to typography.label.badge,
                "label.error" to typography.label.error,
                "action.button" to typography.action.button,
                "action.tab" to typography.action.tab,
                "action.dropdown" to typography.action.dropdown,
                "code.link" to typography.code.link,
                "code.block" to typography.code.block,
            ).forEach { (name, style) ->
                Text(
                    text = name,
                    style = style.copy(color = colors.text.primary),
                )
            }
        }
    }
}

@Composable
private fun ColorGroup(
    name: String,
    tokens: List<Pair<String, Color>>,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = name,
            style = typography.label.section.copy(color = colors.text.primary),
        )

        tokens.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { (tokenName, color) ->
                    ColorSwatch(
                        name = tokenName,
                        color = color,
                        modifier = Modifier.weight(1f),
                    )
                }

                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shape = DeepLinkTheme.shapes.small

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color = color, shape = shape)
                .border(width = 1.dp, color = colors.border.default, shape = shape),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = name,
                style = typography.label.caption.copy(color = colors.text.primary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = color.toHex(),
                style = typography.label.caption.copy(color = colors.text.muted),
            )
        }
    }
}

private fun Color.toHex(): String = "#" + toArgb().toUInt().toString(16).padStart(8, '0').uppercase()
