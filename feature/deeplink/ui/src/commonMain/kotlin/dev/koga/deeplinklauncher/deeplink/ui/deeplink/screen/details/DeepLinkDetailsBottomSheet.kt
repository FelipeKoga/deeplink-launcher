package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DeepLinkDetailsActions
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DuplicateModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.EditModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.LaunchModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.event.DeepLinkDetailsEvent
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkDetailsBottomSheet(
    viewModel: DeepLinkDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmationSheet by rememberSaveable { mutableStateOf(false) }

    if (showDeleteConfirmationSheet) {
        DeleteDeepLinkConfirmationBottomSheet(
            onDismissRequest = { showDeleteConfirmationSheet = false },
            onDelete = {
                showDeleteConfirmationSheet = false
                viewModel.onAction(LaunchAction.Delete)
            },
        )
    }

    DLLModalBottomSheet(onDismiss = { viewModel.navigate(AppNavigationRoute.Back) }) {
        DeepLinkDetailsUI(
            uiState = uiState,
            onDelete = { showDeleteConfirmationSheet = true },
            onAction = viewModel::onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeepLinkDetailsUI(
    uiState: DeepLinkDetailsUiState,
    onAction: (Action) -> Unit,
    onDelete: () -> Unit,
) {

//    DetailsEvents(
//        events = screenModel.events,
//        onDeleted = bottomSheetNavigator::hide,
//        onDuplicated = {
////            bottomSheetNavigator.replace(
////                item = DeepLinkDetailsBottomSheet(
////                    deepLinkId = it.id,
////                    showFolder = showFolder,
////                ),
////            )
//        },
//    )

    SelectionContainer {
        Column {
            DetailsTopBar(
                onBack = { },
                onDelete = onDelete,
                mode = uiState,
            )

            AnimatedContent(
                targetState = uiState,
                contentKey = { uiState::class },
                label = "details_ui_anim",
            ) { target ->
                Column {
                    when (target) {
                        is DeepLinkDetailsUiState.Duplicate -> DuplicateModeUI(
                            uiState = target,
                            onAction = onAction
                        )

                        is DeepLinkDetailsUiState.Edit -> EditModeUI(
                            modifier = Modifier,
                            uiState = target,
                            onAction = onAction,
                        )

                        is DeepLinkDetailsUiState.Launch -> LaunchModeUI(
                            modifier = Modifier,
                            uiState = uiState,
                            onFolderClicked = {},
                        )
                    }
                }
            }

            Column {
                DeepLinkDetailsActions(
                    isFavorite = uiState.deepLink.isFavorite,
                    onAction = onAction,
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    mode: DeepLinkDetailsUiState,
    onBack: () -> Unit,
    onDelete: () -> Unit,
) {
//    val showNavigationIcon by remember(mode) {
//        derivedStateOf { mode.backTo != null }
//    }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (true) {
            DLLIconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = TablerIcons.ArrowLeft,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

//        if (mode is DetailsMode.Edit) {
        DLLIconButton(
            onClick = onDelete,
        ) {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription = "Delete deeplink",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DetailsEvents(
    events: Flow<DeepLinkDetailsEvent>,
    onDeleted: () -> Unit,
    onDuplicated: (DeepLink) -> Unit,
) {
    LaunchedEffect(Unit) {
        events.collectLatest {
            when (it) {
                DeepLinkDetailsEvent.Deleted -> onDeleted()
                is DeepLinkDetailsEvent.Duplicated -> onDuplicated(it.duplicatedDeepLink)
            }
        }
    }
}