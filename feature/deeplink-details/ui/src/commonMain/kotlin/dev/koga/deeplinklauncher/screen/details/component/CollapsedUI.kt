package dev.koga.deeplinklauncher.screen.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.screen.details.state.DeepLinkDetailsUiState
import dev.koga.resources.MR

@Composable
fun DeepLinkDetailsCollapsedUI(
    modifier: Modifier,
    showFolder: Boolean,
    uiState: DeepLinkDetailsUiState,
    onExpand: () -> Unit,
    onFolderClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showFolder && uiState.deepLink.folder != null) {
                ElevatedSuggestionChip(
                    onClick = onFolderClicked,
                    shape = CircleShape,
                    label = {
                        Text(
                            text = uiState.deepLink.folder!!.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    },
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            FilledTonalIconButton(
                modifier = Modifier,
                onClick = onExpand,
            ) {
                Icon(
                    painterResource(MR.images.ic_unfold_more_24dp),
                    contentDescription = "Edit deeplink",
                )
            }
        }

        if (!uiState.deepLink.name.isNullOrBlank()) {
            Text(
                text = uiState.deepLink.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }

        if (!uiState.deepLink.description.isNullOrBlank()) {
            Spacer(modifier = Modifier.padding(top = 2.dp))

            Text(
                text = uiState.deepLink.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Text(
            text = "DeepLink",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
            ),
        )

        Text(
            text = uiState.deepLink.link,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )
    }
}
