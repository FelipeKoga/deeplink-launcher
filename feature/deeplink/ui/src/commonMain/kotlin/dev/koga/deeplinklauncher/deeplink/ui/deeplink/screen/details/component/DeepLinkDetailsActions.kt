package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.Share
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.platform.canShareContent
import dev.koga.resources.Res
import dev.koga.resources.ic_duplicate_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeepLinkDetailsActions(
    isFavorite: Boolean,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onDuplicate: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 8.dp),
    ) {
        DLLIconButton(onClick = onDuplicate) {
            Icon(
                painterResource(Res.drawable.ic_duplicate_24dp),
                contentDescription = "Duplicate",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        if (canShareContent) {
            DLLIconButton(onClick = onShare) {
                Icon(
                    imageVector = TablerIcons.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        DLLIconButton(onClick = onFavorite) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Rounded.Favorite
                } else {
                    Icons.Rounded.FavoriteBorder
                },
                contentDescription = "Favorite",
                tint = if (isFavorite) {
                    Color(0xFFE57373)
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }

        DLLIconButton(onClick = onEdit) {
            Icon(
                imageVector = TablerIcons.Pencil,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onLaunch) {
            Text(
                text = "Launch",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = TablerIcons.ExternalLink,
                contentDescription = "Launch",
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
