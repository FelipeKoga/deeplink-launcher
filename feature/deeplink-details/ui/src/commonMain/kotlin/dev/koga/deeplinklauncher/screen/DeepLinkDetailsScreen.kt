package dev.koga.deeplinklauncher.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import dev.koga.deeplinklauncher.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.navigateToDeepLinkDetails
import dev.koga.deeplinklauncher.screen.component.DeepLinkActionsRow
import dev.koga.deeplinklauncher.screen.component.DeepLinkDetailsCollapsedContent
import dev.koga.deeplinklauncher.screen.component.DeepLinkDetailsExpandedContent
import dev.koga.deeplinklauncher.screen.component.DuplicateDeepLinkBottomSheet
import dev.koga.deeplinklauncher.screen.state.DeepLinkDetailsEvent
import dev.koga.deeplinklauncher.screen.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.usecase.deeplink.DuplicateDeepLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(private val deepLinkId: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalBottomSheetNavigator.current
        val screenModel = getScreenModel<DeepLinkDetailScreenModel>(
            parameters = { parametersOf(deepLinkId) },
        )

        val uiState by screenModel.uiState.collectAsState()
        var detailsMode by remember { mutableStateOf<DetailsMode>(DetailsMode.Collapsed) }
        var bottomSheetState by remember { mutableStateOf<BottomSheetState>(BottomSheetState.Idle) }

        DetailsEventHandler(
            events = screenModel.events,
            onDeleted = navigator::hide,
            onDuplicated = { duplicatedDeepLink ->
                CoroutineScope(Dispatchers.Main).launch {
                    navigator.hide()
                    delay(DELAY_TO_NAVIGATE_AFTER_DISMISS)
                    navigator.navigateToDeepLinkDetails(duplicatedDeepLink.id)
                }
            }
        )

        DetailsBottomSheetsHandler(
            bottomSheetState = bottomSheetState,
            uiState = uiState,
            onDelete = {
                bottomSheetState = BottomSheetState.Idle
                screenModel.delete()
            },
            onDismiss = { bottomSheetState = BottomSheetState.Idle },
            onConfirmDuplication = screenModel::duplicate,
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AnimatedContent(
                targetState = detailsMode,
                label = "",
            ) { target ->
                when (target) {
                    DetailsMode.Collapsed -> DeepLinkDetailsCollapsedContent(
                        modifier = Modifier,
                        uiState = uiState,
                        onExpand = { detailsMode = DetailsMode.Expanded },
                    )

                    DetailsMode.Expanded -> DeepLinkDetailsExpandedContent(
                        modifier = Modifier,
                        uiState = uiState,
                        onNameChanged = screenModel::updateDeepLinkName,
                        onDescriptionChanged = screenModel::updateDeepLinkDescription,
                        onDeleteDeepLink = {
                            bottomSheetState = BottomSheetState.DeleteConfirmation
                        },
                        onAddFolder = screenModel::insertFolder,
                        onSelectFolder = screenModel::selectFolder,
                        onRemoveFolder = screenModel::removeFolderFromDeepLink,
                        onCollapse = { detailsMode = DetailsMode.Collapsed },
                    )
                }
            }

            Divider(modifier = Modifier.padding(top = 24.dp))

            DeepLinkActionsRow(
                link = uiState.deepLink.link,
                isFavorite = uiState.deepLink.isFavorite,
                onShare = screenModel::share,
                onFavorite = screenModel::favorite,
                onLaunch = screenModel::launch,
                onDuplicate = { bottomSheetState = BottomSheetState.Duplicate },
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    companion object {
        private const val BOTTOM_SHEET_DISMISS_ANIM_DURATION = 500L
        private const val DELAY_TO_NAVIGATE_AFTER_DISMISS =
            BOTTOM_SHEET_DISMISS_ANIM_DURATION + 500L
    }

    private sealed interface DetailsMode {
        data object Expanded : DetailsMode
        data object Collapsed : DetailsMode
    }
}

sealed interface BottomSheetState {
    data object Idle : BottomSheetState
    data object DeleteConfirmation : BottomSheetState
    data object Duplicate : BottomSheetState
}


@Composable
private fun DetailsEventHandler(
    events: Flow<DeepLinkDetailsEvent>,
    onDeleted: () -> Unit,
    onDuplicated: (DeepLink) -> Unit,
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                DeepLinkDetailsEvent.Deleted -> onDeleted()
                is DeepLinkDetailsEvent.Duplicated -> onDuplicated(event.duplicatedDeepLink)
            }
        }
    }
}


@Composable
private fun DetailsBottomSheetsHandler(
    bottomSheetState: BottomSheetState,
    uiState: DeepLinkDetailsUiState,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    onConfirmDuplication: (newLink: String, copyAllFields: Boolean) -> Unit,
) {
    when (bottomSheetState) {
        BottomSheetState.Idle -> Unit
        BottomSheetState.DeleteConfirmation -> DeleteDeepLinkConfirmationBottomSheet(
            onDismissRequest = onDismiss,
            onDelete = onDelete
        )

        BottomSheetState.Duplicate -> DuplicateDeepLinkBottomSheet(
            currentLink = uiState.deepLink.link,
            onConfirm = onConfirmDuplication,
            onDismissRequest = onDismiss,
            errorMessage = uiState.duplicateErrorMessage
        )
    }
}