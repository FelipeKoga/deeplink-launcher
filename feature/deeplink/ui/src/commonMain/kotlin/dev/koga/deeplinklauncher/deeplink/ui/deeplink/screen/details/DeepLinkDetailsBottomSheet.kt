package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DuplicateModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.EditModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.LaunchModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.delete.DeepLinkDeleteConfirmationDialog
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsAction
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.EditAction
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkDetailsBottomSheet(
    viewModel: DeepLinkDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        DeepLinkDeleteConfirmationDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            onDelete = { viewModel.onAction(EditAction.Delete) },
        )
    }

    DLLModalBottomSheet(onDismiss = { viewModel.navigate(AppNavigationRoute.Back) }) {
        DeepLinkDetailsUI(
            uiState = uiState,
            onAction = viewModel::onAction,
            onShowDeleteConfirmation = { showDeleteConfirmation = true },
        )
    }
}

@Composable
internal fun DeepLinkDetailsUI(
    uiState: DeepLinkDetailsUiState,
    onAction: (DeepLinkDetailsAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    SelectionContainer {
        Column {
            AnimatedContent(
                targetState = uiState,
                contentKey = { it::class },
                label = "details_ui_anim",
            ) { target ->
                Column {
                    when (target) {
                        is DeepLinkDetailsUiState.Duplicate -> DuplicateModeUI(
                            uiState = target,
                            onAction = onAction,
                        )

                        is DeepLinkDetailsUiState.Edit -> EditModeUI(
                            modifier = Modifier,
                            uiState = target,
                            onAction = onAction,
                            onShowDeleteConfirmation = onShowDeleteConfirmation,
                        )

                        is DeepLinkDetailsUiState.Launch -> LaunchModeUI(
                            modifier = Modifier,
                            uiState = uiState,
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
}
