package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkListItem
import dev.koga.deeplinklauncher.designsystem.DLLSmallChip
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DeepLinkCard(
    modifier: Modifier = Modifier,
    item: DeepLinkListItem,
    onClick: () -> Unit,
    onLaunch: () -> Unit,
    onFolderClicked: () -> Unit = {},
    showFolder: Boolean = true,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val deepLink = item.deepLink

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.border.subtle),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent,
            contentColor = colors.text.primary,
        ),
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DeepLinkHandlerIcon(
                    iconPng = item.iconPng,
                    modifier = Modifier.size(40.dp),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (!deepLink.name.isNullOrBlank()) {
                        Text(
                            text = deepLink.name.orEmpty(),
                            style = typography.title.card.copy(
                                color = colors.text.secondary,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = deepLink.link,
                        style = typography.title.card,
                        maxLines = 3,
                    )

                    if (deepLink.folder != null && showFolder) {
                        Spacer(modifier = Modifier.height(12.dp))

                        DLLSmallChip(
                            label = deepLink.folder!!.name,
                            onClick = onFolderClicked,
                        )
                    }
                }

                DLLIconButton(
                    onClick = onLaunch,
                ) {
                    Icon(
                        imageVector = TablerIcons.ExternalLink,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
