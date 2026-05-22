package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
internal fun LaunchModeUI(
    modifier: Modifier = Modifier,
    uiState: DeepLinkDetailsUiState.Launch,
    onAction: (LaunchAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    val typography = DeepLinkTheme.typography
    val clipboardManager = LocalClipboardManager.current
    val deepLink = uiState.deepLink

    fun copyLink() {
        clipboardManager.setText(AnnotatedString(deepLink.link))
        onAction(LaunchAction.NotifyLinkCopied)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        DetailsHeader(
            deepLink = deepLink,
            icon = uiState.icon,
            metadataHost = uiState.metadata.host,
            createdAt = deepLink.createdAt,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DetailsDeepLinkField(
            link = deepLink.link,
            metadata = uiState.metadata,
            handlerInfo = uiState.handlerInfo,
            icon = uiState.icon,
            description = deepLink.description,
            folder = deepLink.folder,
            folders = uiState.folders,
            showFolder = uiState.showFolder,
            onCopyLink = ::copyLink,
            onFolderClick = { onAction(LaunchAction.NavigateToFolder) },
            onToggleFolder = { onAction(LaunchAction.ToggleFolder(it)) },
            onAddFolder = { onAction(LaunchAction.AddFolder) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailsQuickActions(
            isFavorite = deepLink.isFavorite,
            onAction = onAction,
            onShowDeleteConfirmation = onShowDeleteConfirmation,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DLLButton(
            onClick = { onAction(LaunchAction.Launch) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Launch",
                    style = typography.action.button,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = TablerIcons.ExternalLink,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
