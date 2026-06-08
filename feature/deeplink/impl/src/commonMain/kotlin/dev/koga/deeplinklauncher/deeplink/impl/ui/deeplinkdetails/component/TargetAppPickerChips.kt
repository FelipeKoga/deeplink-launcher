package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TargetAppPickerChips(
    selectedPackage: String?,
    availableHandlers: ImmutableList<DeepLinkHandler>,
    onSelectPackage: (String?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp),
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = contentPadding,
    ) {
        item(key = "default") {
            FilterChip(
                selected = selectedPackage == null,
                onClick = { onSelectPackage(null) },
                label = {
                    Text(
                        text = "Default",
                        style = typography.label.chip,
                    )
                },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface.card,
                    selectedContainerColor = colors.button.primaryBackground,
                    selectedLabelColor = colors.button.primaryContent,
                    selectedTrailingIconColor = colors.button.primaryContent,
                ),
                border = BorderStroke(
                    1.dp,
                    color = colors.surface.elevated,
                ),
                trailingIcon = {
                    if (selectedPackage == null) {
                        Icon(
                            imageVector = TablerIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                },
            )
        }

        items(availableHandlers, key = { it.packageName }) { handler ->
            val selected = selectedPackage == handler.packageName

            FilterChip(
                selected = selected,
                onClick = { onSelectPackage(handler.packageName) },
                label = {
                    Text(
                        text = handler.appName,
                        style = typography.label.chip,
                    )
                },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface.card,
                    selectedContainerColor = colors.button.primaryBackground,
                    selectedLabelColor = colors.button.primaryContent,
                    selectedTrailingIconColor = colors.button.primaryContent,
                ),
                border = BorderStroke(
                    1.dp,
                    color = colors.surface.elevated,
                ),
                trailingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = TablerIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                },
            )
        }

        selectedPackage
            ?.takeIf { packageName -> availableHandlers.none { it.packageName == packageName } }
            ?.let { missingPackage ->
                item(key = missingPackage) {
                    FilterChip(
                        selected = true,
                        onClick = { onSelectPackage(null) },
                        label = {
                            Text(
                                text = missingPackage,
                                style = typography.label.chip,
                            )
                        },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = colors.surface.card,
                            selectedContainerColor = colors.button.primaryBackground,
                            selectedLabelColor = colors.button.primaryContent,
                            selectedTrailingIconColor = colors.button.primaryContent,
                        ),
                        border = BorderStroke(
                            1.dp,
                            color = colors.surface.elevated,
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = TablerIcons.Check,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                            )
                        },
                    )
                }
            }
    }
}

internal fun resolveTargetAppLabel(
    targetPackage: String?,
    availableHandlers: ImmutableList<DeepLinkHandler>,
): String {
    if (targetPackage == null) {
        return "System default"
    }

    return availableHandlers.find { it.packageName == targetPackage }?.appName ?: targetPackage
}

internal fun shouldShowTargetAppPicker(
    availableHandlers: ImmutableList<DeepLinkHandler>,
    targetPackage: String?,
): Boolean = availableHandlers.size >= 1 || targetPackage != null
