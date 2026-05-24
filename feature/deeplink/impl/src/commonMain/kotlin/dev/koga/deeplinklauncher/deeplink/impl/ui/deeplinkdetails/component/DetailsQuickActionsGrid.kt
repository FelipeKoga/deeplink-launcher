package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Home
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.Share
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.designsystem.DLLOutlinedChip
import dev.koga.deeplinklauncher.designsystem.DLLOutlinedChipVariant
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.canShareContent
import dev.koga.deeplinklauncher.platform.currentPlatform
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
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            DLLOutlinedChip(
                label = "",
                icon = if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                variant = if (isFavorite) {
                    DLLOutlinedChipVariant.Accent
                } else {
                    DLLOutlinedChipVariant.Default
                },
                onClick = { onAction(LaunchAction.ToggleFavorite) },
            )
        }

        if (canShareContent) {
            item {
                DLLOutlinedChip(
                    label = "",
                    icon = TablerIcons.Share,
                    onClick = { onAction(LaunchAction.Share) },
                )
            }
        }

        item {
            DLLOutlinedChip(
                label = "",
                icon = TablerIcons.Pencil,
                onClick = { onAction(LaunchAction.Edit) },
            )
        }

        item {
            DLLOutlinedChip(
                label = "",
                iconPainter = painterResource(Res.drawable.ic_duplicate_24dp),
                onClick = { onAction(LaunchAction.Duplicate) },
            )
        }

        if (currentPlatform == Platform.ANDROID) {
            item {
                DLLOutlinedChip(
                    label = "",
                    icon = TablerIcons.Home,
                    onClick = { onAction(LaunchAction.PinToHomeScreen) },
                )
            }
        }

        item {
            DLLOutlinedChip(
                label = "",
                icon = TablerIcons.Trash,
                variant = DLLOutlinedChipVariant.Destructive,
                onClick = onShowDeleteConfirmation,
            )
        }
    }
}
