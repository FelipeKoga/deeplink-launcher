package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.Copy
import compose.icons.tablericons.Plus
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import kotlinx.collections.immutable.ImmutableList

private const val ExpandAnimationDurationMs = 250

@Composable
internal fun DetailsDeepLinkInfo(
    uiState: DeepLinkDetailsUiState.Launch,
    onCopyLink: () -> Unit,
    onFolderClick: () -> Unit = {},
    onToggleFolder: (Folder) -> Unit = {},
    onAddFolder: () -> Unit = {},
    onSelectTargetPackage: (String?) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isFolderPickerExpanded by rememberSaveable { mutableStateOf(false) }

    val folderChevronRotation by animateFloatAsState(
        targetValue = if (isFolderPickerExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = ExpandAnimationDurationMs),
        label = "folder_picker_chevron",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = DeepLinkTheme.shapes.dialog,
            color = Color.Transparent,
            border = BorderStroke(
                width = 1.dp,
                color = DeepLinkTheme.colors.border.subtle,
            ),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCopyLink() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        Text(
                            text = "Deeplink",
                            style = DeepLinkTheme.typography.label.fieldHeader.copy(
                                color = DeepLinkTheme.colors.text.muted,
                            ),
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = uiState.deepLink.link,
                            style = DeepLinkTheme.typography.code.link.copy(
                                color = DeepLinkTheme.colors.surface.primary,
                            ),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = TablerIcons.Copy,
                        contentDescription = "Copy deep link",
                        tint = DeepLinkTheme.colors.surface.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }

                uiState.deepLink.description?.takeIf { it.isNotBlank() }?.let { descriptionText ->
                    DLLHorizontalDivider()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Notes",
                            style = DeepLinkTheme.typography.label.fieldHeader.copy(
                                color = DeepLinkTheme.colors.text.muted,
                            ),
                        )

                        Text(
                            text = descriptionText,
                            style = DeepLinkTheme.typography.body.small.copy(
                                color = DeepLinkTheme.colors.text.primary,
                            ),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }

                if (uiState.showFolder) {
                    DLLHorizontalDivider(thickness = .5.dp)

                    DetailsFolderSection(
                        folder = uiState.deepLink.folder,
                        folders = uiState.folders,
                        isFolderPickerExpanded = isFolderPickerExpanded,
                        folderChevronRotation = folderChevronRotation,
                        onFolderClick = onFolderClick,
                        onToggleFolderPicker = { isFolderPickerExpanded = !isFolderPickerExpanded },
                        onToggleFolder = {
                            onToggleFolder(it)
                            isFolderPickerExpanded = false
                        },
                        onAddFolder = onAddFolder,
                    )
                }

                if (currentPlatform == Platform.ANDROID &&
                    shouldShowTargetAppPicker(uiState.availableHandlers, uiState.deepLink.targetPackage)
                ) {
                    DLLHorizontalDivider(thickness = .5.dp)

                    DetailsTargetAppSection(
                        targetPackage = uiState.deepLink.targetPackage,
                        availableHandlers = uiState.availableHandlers,
                        onSelectTargetPackage = onSelectTargetPackage,
                    )
                }

                DLLHorizontalDivider(thickness = .5.dp)

                DetailsExpandableInfoToggle(
                    isExpanded = isExpanded,
                    metadata = uiState.details.metadata,
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
                            .padding(horizontal = 12.dp),
                    ) {
                        DetailsInformationContent(
                            metadata = uiState.details.metadata,
                            handlerInfo = uiState.details.handlerInfo,
                            icon = uiState.details.icon,
                            targetPackage = uiState.deepLink.targetPackage,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsFolderSection(
    folder: Folder?,
    folders: ImmutableList<Folder>,
    isFolderPickerExpanded: Boolean,
    folderChevronRotation: Float,
    onFolderClick: () -> Unit,
    onToggleFolderPicker: () -> Unit,
    onToggleFolder: (Folder) -> Unit,
    onAddFolder: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (folder != null) {
                        onFolderClick()
                    } else {
                        onToggleFolderPicker()
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Folder",
                    style = typography.label.fieldHeader.copy(
                        color = colors.text.muted,
                    ),
                )

                Text(
                    text = folder?.name ?: "No folder vinculated to the deeplink",
                    style = typography.body.small.copy(
                        color = if (folder != null) {
                            colors.text.primary
                        } else {
                            colors.text.muted
                        },
                        fontWeight = if (folder != null) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Icon(
                imageVector = TablerIcons.ChevronRight,
                contentDescription = if (folder != null) {
                    "Open folder"
                } else if (isFolderPickerExpanded) {
                    "Collapse folder picker"
                } else {
                    "Expand folder picker"
                },
                tint = colors.text.muted,
                modifier = Modifier
                    .size(18.dp)
                    .then(
                        if (folder == null) {
                            Modifier.rotate(folderChevronRotation)
                        } else {
                            Modifier
                        },
                    ),
            )
        }

        if (folder == null) {
            AnimatedVisibility(
                visible = isFolderPickerExpanded,
                enter = expandVertically(
                    animationSpec = tween(ExpandAnimationDurationMs),
                    expandFrom = Alignment.Top,
                ) + fadeIn(animationSpec = tween(ExpandAnimationDurationMs)),
                exit = shrinkVertically(
                    animationSpec = tween(ExpandAnimationDurationMs),
                    shrinkTowards = Alignment.Top,
                ) + fadeOut(animationSpec = tween(ExpandAnimationDurationMs)),
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    item {
                        AssistChip(
                            shape = CircleShape,
                            leadingIcon = {
                                Icon(
                                    imageVector = TablerIcons.Plus,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                )
                            },
                            label = {
                                Text(
                                    text = "Add folder",
                                    style = typography.label.chip,
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = colors.surface.elevated,
                            ),
                            border = null,
                            onClick = onAddFolder,
                        )
                    }

                    items(folders, key = { it.id }) { availableFolder ->
                        FilterChip(
                            selected = false,
                            onClick = { onToggleFolder(availableFolder) },
                            label = {
                                Text(
                                    text = availableFolder.name,
                                    style = typography.label.chip,
                                )
                            },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colors.surface.card,
                            ),
                            border = BorderStroke(
                                1.dp,
                                color = colors.surface.elevated,
                            ),
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
    metadata: DeepLinkMetadata,
    onToggle: () -> Unit,
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = ExpandAnimationDurationMs),
        label = "deeplink_info_chevron",
    )

    val summary = buildList {
        metadata.scheme?.takeIf { it.isNotBlank() }?.let { add(it) }
        metadata.host?.takeIf { it.isNotBlank() }?.let { add(it) }
        metadata.path?.takeIf { it.isNotBlank() }?.let { add(it) }
    }.joinToString(" · ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Info",
                style = DeepLinkTheme.typography.label.fieldHeader.copy(
                    color = DeepLinkTheme.colors.text.muted,
                ),
            )

            if (!isExpanded && summary.isNotBlank()) {
                Text(
                    text = summary,
                    style = DeepLinkTheme.typography.body.small.copy(
                        color = DeepLinkTheme.colors.text.primary,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Icon(
            imageVector = TablerIcons.ChevronRight,
            contentDescription = if (isExpanded) "Collapse details" else "Expand details",
            tint = DeepLinkTheme.colors.text.muted,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer {
                    rotationZ = chevronRotation
                },
        )
    }
}
