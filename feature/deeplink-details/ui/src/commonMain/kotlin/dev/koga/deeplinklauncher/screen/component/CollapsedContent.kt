package dev.koga.deeplinklauncher.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.screen.state.DeepLinkDetailsUiState
import dev.koga.resources.MR

@Composable
fun DeepLinkDetailsCollapsedContent(
    modifier: Modifier,
    uiState: DeepLinkDetailsUiState,
    onExpand: () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            uiState.deepLink.folder?.let {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.1f
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 12.dp
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))


            FilledTonalIconButton(
                modifier = Modifier,
                onClick = onExpand
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
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
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
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
            )
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Text(
            text = uiState.deepLink.link,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )
    }
}