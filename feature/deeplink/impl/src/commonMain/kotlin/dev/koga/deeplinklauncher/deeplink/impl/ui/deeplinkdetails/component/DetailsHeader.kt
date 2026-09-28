package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.DotsVertical
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.date.format
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.ui.DeepLinkHandlerIcon
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

private const val addedAtDateFormat = "MMM d, yyyy 'at' h:mm a"

@Composable
internal fun DetailsHeader(
    uiState: DeepLinkDetailsUiState.Launch,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val addedAtText = "Added ${uiState.deepLink.createdAt.format(addedAtDateFormat)}"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
    ) {
        DeepLinkHandlerIcon(
            icon = uiState.details.icon,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiState.details.displayName,
                style = typography.title.card.copy(
                    color = colors.text.primary,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = addedAtText,
                style = typography.body.small.copy(
                    color = colors.text.muted,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        DetailsOverflowMenu(
            onEdit = onEdit,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun DetailsOverflowMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    var expanded by remember { mutableStateOf(false) }

    Box {
        DLLIconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = TablerIcons.DotsVertical,
                contentDescription = "More actions",
                modifier = Modifier.size(20.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = colors.surface.card,
        ) {
            OverflowMenuItem(
                label = "Edit",
                icon = TablerIcons.Pencil,
                contentColor = colors.text.primary,
                onClick = {
                    expanded = false
                    onEdit()
                },
            )

            OverflowMenuItem(
                label = "Delete",
                icon = TablerIcons.Trash,
                contentColor = colors.text.error,
                onClick = {
                    expanded = false
                    onDelete()
                },
            )
        }
    }
}

@Composable
private fun OverflowMenuItem(
    label: String,
    icon: ImageVector,
    contentColor: Color,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text = label, style = DeepLinkTheme.typography.body.default) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        },
        colors = MenuDefaults.itemColors(
            textColor = contentColor,
            leadingIconColor = contentColor,
        ),
        onClick = onClick,
    )
}
