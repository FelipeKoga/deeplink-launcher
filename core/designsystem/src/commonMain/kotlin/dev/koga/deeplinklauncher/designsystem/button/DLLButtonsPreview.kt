package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.Pencil
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@PreviewLightDark
@Composable
private fun DLLButtonPrimaryPreview() {
    DLLPreviewTheme {
        DLLButtonStates(variant = DLLButtonVariant.Primary)
    }
}

@PreviewLightDark
@Composable
private fun DLLButtonSecondaryPreview() {
    DLLPreviewTheme {
        DLLButtonStates(variant = DLLButtonVariant.Secondary)
    }
}

@PreviewLightDark
@Composable
private fun DLLButtonDestructivePreview() {
    DLLPreviewTheme {
        DLLButtonStates(variant = DLLButtonVariant.Destructive)
    }
}

@PreviewLightDark
@Composable
private fun DLLButtonWithIconPreview() {
    DLLPreviewTheme {
        DLLButton(
            onClick = {},
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Launch",
                style = DeepLinkTheme.typography.action.button,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = TablerIcons.ExternalLink,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DLLTextButtonDefaultPreview() {
    DLLPreviewTheme {
        DLLTextButtonStates(variant = DLLTextButtonVariant.Default)
    }
}

@PreviewLightDark
@Composable
private fun DLLTextButtonDestructivePreview() {
    DLLPreviewTheme {
        DLLTextButtonStates(variant = DLLTextButtonVariant.Destructive)
    }
}

@PreviewLightDark
@Composable
private fun DLLIconButtonDefaultPreview() {
    DLLPreviewTheme {
        StatesRow {
            DLLIconButton(onClick = {}) {
                Icon(
                    imageVector = TablerIcons.Pencil,
                    contentDescription = "Edit",
                )
            }

            DLLIconButton(
                onClick = {},
                enabled = false,
            ) {
                Icon(
                    imageVector = TablerIcons.Pencil,
                    contentDescription = "Edit",
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DLLFilledIconButtonDefaultPreview() {
    DLLPreviewTheme {
        StatesRow {
            DLLFilledIconButton(onClick = {}) {
                Icon(
                    imageVector = TablerIcons.Check,
                    contentDescription = "Save",
                )
            }

            DLLFilledIconButton(
                onClick = {},
                enabled = false,
            ) {
                Icon(
                    imageVector = TablerIcons.Check,
                    contentDescription = "Save",
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DLLOutlinedIconButtonDefaultPreview() {
    DLLPreviewTheme {
        StatesRow {
            DLLOutlinedIconButton(onClick = {}) {
                Icon(
                    imageVector = TablerIcons.ExternalLink,
                    contentDescription = "Launch",
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun DLLButtonStates(variant: DLLButtonVariant) {
    StatesRow {
        DLLButton(
            onClick = {},
            text = "Enabled",
            variant = variant,
        )

        DLLButton(
            onClick = {},
            text = "Disabled",
            enabled = false,
            variant = variant,
        )
    }
}

@Composable
private fun DLLTextButtonStates(variant: DLLTextButtonVariant) {
    StatesRow {
        DLLTextButton(
            onClick = {},
            text = "Enabled",
            variant = variant,
        )

        DLLTextButton(
            onClick = {},
            text = "Disabled",
            enabled = false,
            variant = variant,
        )
    }
}

@Composable
private fun StatesRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
