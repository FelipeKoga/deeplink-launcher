package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import kotlinx.collections.immutable.ImmutableList

private const val ExpandAnimationDurationMs = 250

@Composable
internal fun DetailsTargetAppSection(
    targetPackage: String?,
    availableHandlers: ImmutableList<DeepLinkHandler>,
    onSelectTargetPackage: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (currentPlatform != Platform.ANDROID) {
        return
    }

    if (!shouldShowTargetAppPicker(availableHandlers, targetPackage)) {
        return
    }

    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = ExpandAnimationDurationMs),
        label = "target_app_chevron",
    )
    val targetLabel = resolveTargetAppLabel(targetPackage, availableHandlers)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Target app",
                    style = typography.label.fieldHeader.copy(
                        color = colors.text.muted,
                    ),
                )

                Text(
                    text = targetLabel,
                    style = typography.body.small.copy(
                        color = colors.text.primary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Icon(
                imageVector = TablerIcons.ChevronRight,
                contentDescription = if (isExpanded) {
                    "Collapse target app picker"
                } else {
                    "Expand target app picker"
                },
                tint = colors.text.muted,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(chevronRotation),
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = tween(ExpandAnimationDurationMs),
                expandFrom = Alignment.Top,
            ) + fadeIn(animationSpec = tween(ExpandAnimationDurationMs)),
            exit = shrinkVertically(
                animationSpec = tween(ExpandAnimationDurationMs),
                shrinkTowards = Alignment.Top,
            ) + fadeOut(animationSpec = tween(ExpandAnimationDurationMs)),
        ) {
            TargetAppPickerChips(
                selectedPackage = targetPackage,
                availableHandlers = availableHandlers,
                onSelectPackage = {
                    onSelectTargetPackage(it)
                    isExpanded = false
                },
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                ),
            )
        }
    }
}
