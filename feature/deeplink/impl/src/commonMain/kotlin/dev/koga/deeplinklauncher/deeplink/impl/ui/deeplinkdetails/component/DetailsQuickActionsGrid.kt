package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Dots
import compose.icons.tablericons.Home
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.Share
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.canShareContent
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.resources.Res
import dev.koga.resources.ic_duplicate_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun DetailsQuickActionsGrid(
    isFavorite: Boolean,
    onAction: (LaunchAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMoreMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        QuickActionTile(
            label = "Favorite",
            icon = if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarOutline,
            iconColor = if (isFavorite) Color(0xFFFFB300) else Color.Unspecified,
            onClick = { onAction(LaunchAction.ToggleFavorite) },
            modifier = Modifier.weight(1f),
        )

        if (canShareContent) {
            QuickActionTile(
                label = "Share",
                icon = TablerIcons.Share,
                onClick = { onAction(LaunchAction.Share) },
                modifier = Modifier.weight(1f),
            )
        }

        QuickActionTile(
            label = "Edit",
            icon = TablerIcons.Pencil,
            onClick = { onAction(LaunchAction.Edit) },
            modifier = Modifier.weight(1f),
        )

        Box(modifier = Modifier.weight(1f)) {
            QuickActionTile(
                label = "More",
                icon = TablerIcons.Dots,
                onClick = { showMoreMenu = true },
                modifier = Modifier.fillMaxWidth(),
            )

            DetailsMoreDropdownMenu(
                expanded = showMoreMenu,
                onDismissRequest = { showMoreMenu = false },
                onDuplicate = {
                    showMoreMenu = false
                    onAction(LaunchAction.Duplicate)
                },
                onPinToHome = {
                    showMoreMenu = false
                    onAction(LaunchAction.PinToHomeScreen)
                },
                onAddToShortcut = {
                    showMoreMenu = false
                    onAction(LaunchAction.AddToShortCut)
                },
                onDelete = {
                    showMoreMenu = false
                    onShowDeleteConfirmation()
                },
            )
        }
    }
}

@Composable
private fun DetailsMoreDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDuplicate: () -> Unit,
    onPinToHome: () -> Unit,
    onAddToShortcut: () -> Unit,
    onDelete: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.width(220.dp),
        shape = RoundedCornerShape(10.dp),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        ),
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            DetailsMoreMenuItem(
                label = "Duplicate",
                iconPainter = painterResource(Res.drawable.ic_duplicate_24dp),
                onClick = onDuplicate,
            )

            if (currentPlatform == Platform.ANDROID) {
                DetailsMoreMenuItem(
                    label = "Add to home",
                    icon = TablerIcons.Home,
                    onClick = onPinToHome,
                )

//                DetailsMoreMenuItem(
//                    label = "Add to shortcut",
//                    icon = Icons.Rounded.AppShortcut,
//                    onClick = onAddToShortcut,
//                )
            }

            MoreMenuDivider()

            DetailsMoreMenuItem(
                label = "Delete deep link",
                icon = TablerIcons.Trash,
                destructive = true,
                onClick = onDelete,
            )
        }
    }
}

@Composable
private fun DetailsMoreMenuItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    destructive: Boolean = false,
) {
    val contentColor = if (destructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val iconTint = if (destructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        color = Color.Unspecified,
        shape = RoundedCornerShape(6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }

                iconPainter != null -> {
                    Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (destructive) FontWeight.Medium else FontWeight.Normal,
                    color = contentColor,
                ),
                modifier = Modifier.padding(start = 10.dp),
            )
        }
    }
}

@Composable
private fun MoreMenuDivider() {
    DLLHorizontalDivider(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun QuickActionTile(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconColor: Color = Color.Unspecified,
) {
    Surface(
        modifier = modifier.height(72.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (iconColor == Color.Unspecified) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        iconColor
                    },
                    modifier = Modifier.size(20.dp),
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}
