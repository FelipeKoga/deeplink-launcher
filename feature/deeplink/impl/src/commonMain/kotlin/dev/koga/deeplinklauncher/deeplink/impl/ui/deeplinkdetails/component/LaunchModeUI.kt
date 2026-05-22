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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction

@Composable
internal fun LaunchModeUI(
    modifier: Modifier = Modifier,
    uiState: DeepLinkDetailsUiState.Launch,
    onAction: (LaunchAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
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
            iconPng = uiState.iconPng,
            metadataHost = uiState.metadata.host,
            createdAt = deepLink.createdAt,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DetailsQuickActionsGrid(
            isFavorite = deepLink.isFavorite,
            onAction = onAction,
            onShowDeleteConfirmation = onShowDeleteConfirmation,
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailsDeepLinkField(
            link = deepLink.link,
            metadata = uiState.metadata,
            handlerInfo = uiState.handlerInfo,
            iconPng = uiState.iconPng,
            onCopyLink = ::copyLink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAction(LaunchAction.Launch) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Launch",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
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
