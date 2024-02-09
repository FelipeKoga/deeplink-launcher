package dev.koga.deeplinklauncher.deeplink

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.resources.MR
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkActionsBottomSheet(
    deepLink: DeepLink,
    onDetails: () -> Unit,
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            IconButton(
                onClick = onDetails,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(MR.images.ic_chevron_right_24dp),
                    contentDescription = "Show more details"
                )
            }


            deepLink.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                )
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = deepLink.link,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
            )

            Divider(modifier = Modifier.padding(top = 24.dp))

            DeepLinkActionsRow(
                link = deepLink.link,
                isFavorite = deepLink.isFavorite,
                onShare = onShare,
                onFavorite = onFavorite,
                onLaunch = onLaunch,
                onDuplicate = {}
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}