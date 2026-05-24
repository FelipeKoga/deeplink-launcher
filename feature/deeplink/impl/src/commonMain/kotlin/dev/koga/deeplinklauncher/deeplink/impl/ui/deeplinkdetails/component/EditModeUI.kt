package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Check
import compose.icons.tablericons.Plus
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.EditAction
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
internal fun EditModeUI(
    modifier: Modifier = Modifier,
    uiState: DeepLinkDetailsUiState.Edit,
    onAction: (EditAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val deepLink = uiState.deepLink

    Column(modifier = modifier) {
        EditTopBar(
            onBack = { onAction(EditAction.Back) },
            onDelete = onShowDeleteConfirmation,
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
        ) {
            DeepLinkDetailsTextField(
                text = deepLink.name.orEmpty(),
                onTextChange = { onAction(EditAction.OnNameChanged(it)) },
                label = "Name",
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeepLinkDetailsTextField(
                text = deepLink.description.orEmpty(),
                onTextChange = { onAction(EditAction.OnDescriptionChanged(it)) },
                label = "Description",
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeepLinkDetailsTextField(
                text = deepLink.link,
                onTextChange = { onAction(EditAction.OnLinkChanged(it)) },
                label = "Link",
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
            ) {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    style = typography.label.error.copy(
                        color = colors.text.error,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
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
                    onClick = { onAction(EditAction.AddFolder) },
                )
            }

            items(uiState.folders) { folder ->
                val selected = uiState.deepLink.folder?.id == folder.id

                FilterChip(
                    selected = selected,
                    onClick = { onAction(EditAction.ToggleFolder(folder)) },
                    label = {
                        Text(
                            text = folder.name,
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
        }

        Spacer(modifier = Modifier.padding(vertical = 24.dp))
    }
}

@Composable
internal fun DeepLinkDetailsTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
) {
    var textState by rememberSaveable { mutableStateOf(text) }

    DLLTextField(
        value = textState,
        onValueChange = {
            textState = it
            onTextChange(it)
        },
        label = label,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun EditTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = DeepLinkTheme.colors

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        DLLIconButton(
            onClick = onBack,
        ) {
            Icon(
                imageVector = TablerIcons.ArrowLeft,
                contentDescription = "Back",
                tint = colors.surface.primary,
            )
        }
    }
}
