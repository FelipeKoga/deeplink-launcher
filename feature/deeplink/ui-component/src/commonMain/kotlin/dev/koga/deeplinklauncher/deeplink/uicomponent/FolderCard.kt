package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.designsystem.DLLOutlinedCard
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.resources.Res
import dev.koga.resources.folder_deeplink_count
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun FolderCard(
    modifier: Modifier = Modifier,
    folder: Folder,
    onClick: (Folder) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes

    DLLOutlinedCard(
        onClick = { onClick(folder) },
        modifier = modifier.size(184.dp),
        shape = shapes.cardLarge,
        containerColor = colors.surface.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = folder.name,
                style = typography.title.dialog.copy(
                    color = colors.surface.primary,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = if (folder.deepLinkCount > 0) {
                    pluralStringResource(
                        resource = Res.plurals.folder_deeplink_count,
                        quantity = folder.deepLinkCount,
                        folder.deepLinkCount,
                    )
                } else {
                    "No deeplinks"
                },
                style = typography.body.smallEmphasis.copy(
                    color = colors.text.secondary,
                ),
            )
        }
    }
}
