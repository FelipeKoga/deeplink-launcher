package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.DuplicateModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.EditModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.LaunchModeUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.WindowlessBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component.rememberWindowlessBottomSheetState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete.DeepLinkDeleteConfirmationDialog
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DuplicateAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.EditAction
import dev.koga.deeplinklauncher.designsystem.DLLSnackbarHost
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun DeepLinkDetailsBottomSheet(
    viewModel: DeepLinkDetailsViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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

    val sheetState = rememberWindowlessBottomSheetState()

    NavigationBackHandler(
        state = rememberNavigationEventState(currentInfo = NavigationEventInfo.None),
    ) {
        when (uiState) {
            is DeepLinkDetailsUiState.Launch -> sheetState.dismiss()
            is DeepLinkDetailsUiState.Edit -> viewModel.onAction(EditAction.Back)
            is DeepLinkDetailsUiState.Duplicate -> viewModel.onAction(DuplicateAction.Back)
        }
    }

    WindowlessBottomSheet(
        state = sheetState,
        onDismissed = { viewModel.popBackStack() },
        containerColor = DeepLinkTheme.colors.surface.card,
        contentReady = uiState.deepLink.id.isNotEmpty(),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            DeepLinkDetailsUI(
                uiState = uiState,
                scrollState = scrollState,
                onAction = viewModel::onAction,
                onShowDeleteConfirmation = { showDeleteConfirmation = true },
            )

            DLLSnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackBarHostState,
            )
        }
    }
}

@Composable
internal fun DeepLinkDetailsUI(
    modifier: Modifier = Modifier,
    uiState: DeepLinkDetailsUiState,
    scrollState: ScrollState,
    onAction: (DeepLinkDetailsAction) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    SelectionContainer {
        Column(modifier = modifier.verticalScroll(scrollState)) {
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
