package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Bolt
import compose.icons.tablericons.Home
import compose.icons.tablericons.Share
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
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
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        QuickAction(
            label = "Favorite",
            painter = rememberVectorPainter(
                if (isFavorite) Icons.Rounded.Star else Icons.Rounded.StarOutline,
            ),
            checked = isFavorite,
            checkedTint = colors.accent.favorite,
            checkedBackground = colors.accent.favorite.copy(alpha = FAVORITE_BACKGROUND_ALPHA),
            onClick = { onAction(LaunchAction.ToggleFavorite) },
        )

        if (canShareContent) {
            QuickAction(
                label = "Share",
                painter = rememberVectorPainter(TablerIcons.Share),
                onClick = { onAction(LaunchAction.Share) },
            )
        }

        QuickAction(
            label = "Duplicate",
            painter = painterResource(Res.drawable.ic_duplicate_24dp),
            onClick = { onAction(LaunchAction.Duplicate) },
        )

        if (currentPlatform == Platform.ANDROID) {
            QuickAction(
                label = "Add to Home",
                painter = rememberVectorPainter(TablerIcons.Home),
                onClick = { onAction(LaunchAction.PinToHomeScreen) },
            )

            QuickAction(
                label = "Shortcut",
                painter = rememberVectorPainter(TablerIcons.Bolt),
                onClick = { onAction(LaunchAction.AddToShortCut) },
            )
        }
    }
}

@Composable
private fun RowScope.QuickAction(
    label: String,
    painter: Painter,
    onClick: () -> Unit,
    checked: Boolean? = null,
    checkedTint: Color = Color.Unspecified,
    checkedBackground: Color = Color.Unspecified,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val interaction = if (checked == null) {
        Modifier.clickable(role = Role.Button, onClick = onClick)
    } else {
        Modifier.toggleable(value = checked, role = Role.Checkbox, onValueChange = { onClick() })
    }
    val tint = if (checked == true) checkedTint else colors.text.primary
    val background = if (checked == true) checkedBackground else colors.surface.elevated

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .clip(DeepLinkTheme.shapes.action)
            .then(interaction)
            .padding(horizontal = 2.dp, vertical = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(background),
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            style = typography.body.small.copy(color = colors.text.secondary),
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private const val FAVORITE_BACKGROUND_ALPHA = 0.16f
