package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Home
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.Share
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.canShareContent
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.resources.Res
import dev.koga.resources.ic_duplicate_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun DetailsQuickActions(
    isFavorite: Boolean,
    onAction: (LaunchAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            QuickActionChip(
                label = "Favorite",
                icon = if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                iconColor = if (isFavorite) colors.accent.favorite else Color.Unspecified,
                onClick = { onAction(LaunchAction.ToggleFavorite) },
            )
        }

        if (canShareContent) {
            item {
                QuickActionChip(
                    label = "Share",
                    icon = TablerIcons.Share,
                    onClick = { onAction(LaunchAction.Share) },
                )
            }
        }

        item {
            QuickActionChip(
                label = "Edit",
                icon = TablerIcons.Pencil,
                onClick = { onAction(LaunchAction.Edit) },
            )
        }

        item {
            QuickActionChip(
                label = "Duplicate",
                iconPainter = painterResource(Res.drawable.ic_duplicate_24dp),
                onClick = { onAction(LaunchAction.Duplicate) },
            )
        }

        if (currentPlatform == Platform.ANDROID) {
            item {
                QuickActionChip(
                    label = "Add to home",
                    icon = TablerIcons.Home,
                    onClick = { onAction(LaunchAction.PinToHomeScreen) },
                )
            }
        }

        item {
            QuickActionChip(
                label = "Delete",
                icon = TablerIcons.Trash,
                destructive = true,
                onClick = onShowDeleteConfirmation,
            )
        }
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    iconColor: Color = Color.Unspecified,
    destructive: Boolean = false,
) {
    val colors = DeepLinkTheme.colors
    val contentColor = if (destructive) {
        colors.text.error
    } else {
        colors.text.primary
    }
    val iconTint = when {
        destructive -> colors.text.error
        iconColor != Color.Unspecified -> iconColor
        else -> colors.text.primary
    }
    val borderColor = if (destructive) {
        colors.text.error.copy(alpha = 0.4f)
    } else {
        colors.border.subtle
    }

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(percent = 50),
        color = colors.surface.muted,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    fontWeight = FontWeight.Normal,
                    color = contentColor,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
