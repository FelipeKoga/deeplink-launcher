package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.FolderListItem
import dev.koga.deeplinklauncher.designsystem.DLLOutlinedCard
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.resources.Res
import dev.koga.resources.folder_deeplink_count
import dev.koga.resources.folder_empty
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

const val MAX_FOLDER_PREVIEW_ICONS = 4
val FOLDER_CARD_HEIGHT = 180.dp

@Composable
fun FolderCard(
    item: FolderListItem,
    onClick: (Folder) -> Unit,
    modifier: Modifier = Modifier,
) {
    val folder = item.folder
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes
    val overflowCount = folder.deepLinkCount - MAX_FOLDER_PREVIEW_ICONS

    DLLOutlinedCard(
        onClick = { onClick(folder) },
        modifier = modifier
            .fillMaxWidth()
            .height(FOLDER_CARD_HEIGHT),
        shape = shapes.cardLarge,
        containerColor = colors.surface.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shapes.icon)
                    .background(colors.surface.muted),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = TablerIcons.Folder,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.text.primary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = folder.name,
                style = typography.title.card.copy(color = colors.text.primary),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (folder.deepLinkCount > 0) {
                    pluralStringResource(
                        resource = Res.plurals.folder_deeplink_count,
                        quantity = folder.deepLinkCount,
                        folder.deepLinkCount,
                    )
                } else {
                    stringResource(Res.string.folder_empty)
                },
                style = typography.body.smallEmphasis.copy(color = colors.text.secondary),
                textAlign = TextAlign.Center,
            )

            if (currentPlatform == Platform.ANDROID && folder.deepLinkCount > 0) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    item.previewIcons.forEach { icon ->
                        DeepLinkHandlerIcon(
                            icon = icon,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    if (overflowCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(shapes.small)
                                .background(colors.surface.muted),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "+$overflowCount",
                                style = typography.label.badge.copy(color = colors.text.secondary),
                            )
                        }
                    }
                }
            }
        }
    }
}
