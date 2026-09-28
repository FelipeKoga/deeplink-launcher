package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.date.formatRelativeToNow
import dev.koga.deeplinklauncher.deeplink.uicomponent.formatting.truncatedLink
import dev.koga.deeplinklauncher.deeplink.uicomponent.model.DeepLinkListItem
import dev.koga.deeplinklauncher.designsystem.DLLOutlinedCard
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.button.DLLOutlinedIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.datetime.LocalDateTime

@Composable
public fun DeepLinkCard(
    item: DeepLinkListItem,
    onClick: () -> Unit,
    actions: DeepLinkCardActions,
    modifier: Modifier = Modifier,
    showFolder: Boolean = true,
    onFolderClicked: () -> Unit = {},
    painters: DeepLinkCardPainters = rememberDeepLinkCardPainters(),
    now: LocalDateTime = currentLocalDateTime,
) {
    DLLOutlinedCard(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DeepLinkCardContent(
                item = item,
                showFolder = showFolder,
                onFolderClicked = onFolderClicked,
                painters = painters,
                now = now,
                modifier = Modifier.weight(1f),
            )

            if (actions.hasActions) {
                Spacer(modifier = Modifier.width(8.dp))

                DeepLinkCardTrailingActions(
                    item = item,
                    actions = actions,
                    painters = painters,
                )
            }
        }
    }
}

@Composable
internal fun DeepLinkCardContent(
    item: DeepLinkListItem,
    showFolder: Boolean,
    onFolderClicked: () -> Unit,
    painters: DeepLinkCardPainters,
    now: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val deepLink = item.deepLink
    val displayTitle = deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.truncatedLink()
    val timestamp = (deepLink.lastLaunchedAt ?: deepLink.createdAt).formatRelativeToNow(now)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DeepLinkHandlerIcon(
            icon = item.icon,
            modifier = Modifier.size(40.dp),
            fallbackPainter = painters.linkFallback,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayTitle,
                style = typography.title.card.copy(color = colors.text.primary),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (!deepLink.name.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = deepLink.truncatedLink(),
                    style = typography.code.link.copy(color = colors.text.muted),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (deepLink.folder != null && showFolder) {
                    Row(
                        modifier = Modifier.clickable(onClick = onFolderClicked),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            painter = painters.folder,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = colors.text.muted,
                        )

                        Text(
                            text = deepLink.folder!!.name,
                            style = typography.label.caption.copy(color = colors.text.muted),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Text(
                        text = "·",
                        style = typography.label.caption.copy(color = colors.text.muted),
                    )
                }

                Text(
                    text = timestamp,
                    style = typography.label.caption.copy(color = colors.text.muted),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun DeepLinkCardTrailingActions(
    item: DeepLinkListItem,
    actions: DeepLinkCardActions,
    painters: DeepLinkCardPainters,
) {
    val colors = DeepLinkTheme.colors
    val deepLink = item.deepLink

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        actions.onToggleFavorite?.let { onToggleFavorite ->
            DLLIconButton(onClick = onToggleFavorite) {
                Icon(
                    painter = if (deepLink.isFavorite) painters.star else painters.starOutline,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (deepLink.isFavorite) {
                        colors.accent.favorite
                    } else {
                        colors.text.muted
                    },
                )
            }
        }

        actions.onLaunch?.let { onLaunch ->
            DLLOutlinedIconButton(onClick = onLaunch) {
                Icon(
                    painter = painters.launch,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
