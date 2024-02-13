package dev.koga.deeplinklauncher.deeplink

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.theme.primaryTextColor
import dev.koga.deeplinklauncher.theme.secondaryTextColor
import dev.koga.resources.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkItem(
    modifier: Modifier = Modifier,
    deepLink: DeepLink,
    onClick: () -> Unit,
    onLaunch: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(0.1f)),
    ) {
        Column {
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    deepLink.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = secondaryTextColor,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    deepLink.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Light,
                                color = secondaryTextColor,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    if (deepLink.hasNameOrDescription) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = deepLink.link,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                            color = primaryTextColor,
                        ),
                    )
                }

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
