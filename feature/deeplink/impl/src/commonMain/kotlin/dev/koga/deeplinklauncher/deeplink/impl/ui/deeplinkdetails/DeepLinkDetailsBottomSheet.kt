package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.DuplicateModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.EditModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.LaunchModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete.DeepLinkDeleteConfirmationDialog
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.EditAction
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeepLinkDetailsBottomSheet(
    viewModel: DeepLinkDetailsViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        DeepLinkDeleteConfirmationDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            onDelete = { viewModel.onAction(EditAction.Delete) },
        )
    }

    LaunchedEffect(Unit) {
        viewModel.messages
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {
                snackBarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short,
                )
            }
    }

    DLLModalBottomSheet(
        onDismiss = { viewModel.popBackStack() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            DeepLinkDetailsUI(
                uiState = uiState,
                onAction = viewModel::onAction,
                onShowDeleteConfirmation = { showDeleteConfirmation = true },
            )

            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackBarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        containerColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = .95f,
                        ),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                },
            )
        }
    }
}

@Composable
internal fun DeepLinkDetailsUI(
    modifier: Modifier = Modifier,
    uiState: DeepLinkDetailsUiState,
    onAction: (DeepLinkDetailsAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    SelectionContainer {
        Column(modifier = modifier) {
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
                            uiState = target,
                            onAction = onAction,
                            onShowDeleteConfirmation = onShowDeleteConfirmation,
                        )
                    }
                }
            }
        }
    }
}
