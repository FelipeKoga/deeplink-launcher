package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.Settings
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@PreviewLightDark
@Composable
private fun DLLListItemWithTrailingContentPreview() {
    DLLPreviewTheme {
        DLLListItem(
            title = "Theme",
            description = "Customize the appearance of the app",
            trailingContent = {
                ChevronIcon()
            },
        )

        DLLHorizontalDivider()

        DLLListItem(
            title = "Suggestions",
            description = "Enable or disable deeplink suggestions when typing a deeplink",
            trailingContent = {
                ChevronIcon()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLListItemWithoutTrailingContentPreview() {
    DLLPreviewTheme {
        DLLListItem(
            title = "Open source licenses",
            description = "Libraries used to build this app",
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLOutlinedCardClickablePreview() {
    DLLPreviewTheme {
        DLLOutlinedCard(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            OutlinedCardSampleContent(
                title = "Clickable card",
                description = "Rendered with the subtle border",
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DLLOutlinedCardStaticPreview() {
    DLLPreviewTheme {
        DLLOutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            OutlinedCardSampleContent(
                title = "Static card",
                description = "Rendered with the default border",
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DLLCodeBlockJsonPreview() {
    DLLPreviewTheme {
        DLLCodeBlock(
            text = codeBlockSample,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun DLLTopBarWithNavigationIconPreview() {
    DLLPreviewTheme {
        DLLTopBar(
            title = {
                DLLTopBarDefaults.Title(text = "Export DeepLinks")
            },
            navigationIcon = {
                DLLTopBarDefaults.NavigationIcon(onClicked = {})
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun DLLTopBarWithActionsPreview() {
    DLLPreviewTheme {
        DLLTopBar(
            title = {
                DLLTopBarDefaults.Title(text = "DeepLink Launcher")
            },
            actions = {
                DLLIconButton(onClick = {}) {
                    Icon(
                        imageVector = TablerIcons.Settings,
                        contentDescription = "Settings",
                    )
                }
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLSnackbarMessagePreview() {
    DLLPreviewTheme {
        DLLSnackbar(
            snackbarData = PreviewSnackbarData,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun ChevronIcon() {
    Icon(
        imageVector = TablerIcons.ChevronRight,
        contentDescription = "navigate",
        tint = DeepLinkTheme.colors.text.primary,
    )
}

@Composable
private fun OutlinedCardSampleContent(
    title: String,
    description: String,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = typography.title.card.copy(color = colors.text.primary),
        )

        Text(
            text = description,
            style = typography.body.small.copy(color = colors.text.muted),
        )
    }
}

private val codeBlockSample = """
    [
      {
        "name": "Product details",
        "link": "shop://products/42",
        "folder": "Shop"
      }
    ]
""".trimIndent()

private object PreviewSnackbarVisuals : SnackbarVisuals {
    override val message: String = "Thank you for your support!"
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

private object PreviewSnackbarData : SnackbarData {
    override val visuals: SnackbarVisuals = PreviewSnackbarVisuals

    override fun performAction() = Unit

    override fun dismiss() = Unit
}
