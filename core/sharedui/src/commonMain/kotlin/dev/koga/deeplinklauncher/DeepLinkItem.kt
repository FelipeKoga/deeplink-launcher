package dev.koga.deeplinklauncher

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import dev.koga.resources.MR
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject

@Composable
fun DeepLinkItem(
    modifier: Modifier = Modifier,
    deepLinkClipboardProvider: DeepLinkClipboardProvider = koinInject(),
    deepLink: DeepLink,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
) {
    DeepLinkCard(
        deepLink = deepLink,
        modifier = modifier,
        onLongClick = {
            deepLinkClipboardProvider.copy(deepLink.link)
        },
        onClick = {
            onClick(deepLink)
        },
        onLaunch = {
            onLaunch(deepLink)
        }
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
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                deepLink.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                deepLink.folder?.let {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                        )
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = deepLink.link,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(onClick = onLaunch) {
                    Icon(
                        painterResource(MR.images.ic_launch_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }

            deepLink.description?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }
    }
}
