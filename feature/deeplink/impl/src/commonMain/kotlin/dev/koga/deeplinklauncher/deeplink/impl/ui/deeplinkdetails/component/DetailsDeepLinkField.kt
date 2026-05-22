package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import compose.icons.tablericons.Copy
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider

private const val ExpandAnimationDurationMs = 250

@Composable
internal fun DetailsDeepLinkField(
    link: String,
    metadata: DeepLinkMetadata,
    handlerInfo: DeepLinkHandlerInfo,
    iconPng: ByteArray?,
    onCopyLink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = ExpandAnimationDurationMs),
        label = "deeplink_info_chevron",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Deeplink",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(durationMillis = ExpandAnimationDurationMs),
                ),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
            ),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCopyLink() }
                        .padding(horizontal = 12.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = link,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    Icon(
                        imageVector = TablerIcons.Copy,
                        contentDescription = "Copy deep link",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }

                DLLHorizontalDivider(thickness = .7.dp)

                DetailsExpandableInfoToggle(
                    isExpanded = isExpanded,
                    chevronRotation = chevronRotation,
                    metadata = metadata,
                    handlerInfo = handlerInfo,
                    onToggle = { isExpanded = !isExpanded },
                )

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                    ) {
                        DLLHorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

                        DetailsInformationContent(
                            metadata = metadata,
                            handlerInfo = handlerInfo,
                            iconPng = iconPng,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsExpandableInfoToggle(
    isExpanded: Boolean,
    chevronRotation: Float,
    metadata: DeepLinkMetadata,
    handlerInfo: DeepLinkHandlerInfo,
    onToggle: () -> Unit,
) {
    val summaryParts = buildList {
        metadata.scheme?.takeIf { it.isNotBlank() }?.let { add(it) }
        metadata.host?.takeIf { it.isNotBlank() }?.let { add(it) }
        metadata.path?.takeIf { it.isNotBlank() }?.let { add(it) }
    }
    val summary = summaryParts.joinToString(" · ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isExpanded) "Hide breakdown" else "Show breakdown",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )

            AnimatedVisibility(visible = !isExpanded) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = TablerIcons.ChevronRight,
            contentDescription = if (isExpanded) "Collapse details" else "Expand details",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(18.dp)
                .rotate(chevronRotation),
        )
    }
}
