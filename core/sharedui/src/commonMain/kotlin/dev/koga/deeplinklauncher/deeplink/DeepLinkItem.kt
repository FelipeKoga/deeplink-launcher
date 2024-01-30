package dev.koga.deeplinklauncher.deeplink

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.resources.MR

@Composable
fun DeepLinkItem(
    modifier: Modifier = Modifier,
    deepLink: DeepLink,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onCopy: (DeepLink) -> Unit,
) {
    DeepLinkCard(
        deepLink = deepLink,
        modifier = modifier,
        onLongClick = {
            onCopy(deepLink)
        },
        onClick = {
            onClick(deepLink)
        },
        onLaunch = {
            onLaunch(deepLink)
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeepLinkCard(
    modifier: Modifier = Modifier,
    deepLink: DeepLink,
    onClick: () -> Unit,
    onLaunch: () -> Unit,
    onLongClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            deepLink.folder?.let {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(0.1f)),
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
            ) {
                deepLink.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }

                deepLink.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                        ),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = deepLink.link,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.weight(1f),
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    FilledTonalIconButton(onClick = onLaunch) {
                        Icon(
                            painterResource(MR.images.ic_launch_24dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}
