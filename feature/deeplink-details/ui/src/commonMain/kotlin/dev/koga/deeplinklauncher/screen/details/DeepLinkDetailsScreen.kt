package dev.koga.deeplinklauncher.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkActionsRow
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkDetailsCollapsedUI
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkDetailsExpandedUI
import dev.koga.deeplinklauncher.screen.details.component.DuplicateDeepLinkUI
import dev.koga.deeplinklauncher.screen.details.event.DeepLinkDetailsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(
    private val deepLinkId: String,
    private val showFolder: Boolean,
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow.parent
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val screenModel = getScreenModel<DeepLinkDetailsScreenModel>(
            parameters = { parametersOf(deepLinkId) },
        )
        val uiState by screenModel.uiState.collectAsState()
        var detailsMode by remember { mutableStateOf<DetailsMode>(DetailsMode.Collapsed) }
        var showDeleteBottomSheet by rememberSaveable { mutableStateOf(false) }

        if (showDeleteBottomSheet) {
            DeleteDeepLinkConfirmationBottomSheet(
                onDismissRequest = { showDeleteBottomSheet = false },
                onDelete = {
                    showDeleteBottomSheet = false
                    screenModel.delete()
                },
            )
        }

        DetailsEvents(
            events = screenModel.events,
            onDeleted = bottomSheetNavigator::hide,
            onDuplicated = {
                CoroutineScope(Dispatchers.Main).launch {
                    bottomSheetNavigator.hide()
                    delay(DELAY_BEFORE_SHOW_DUPLICATE)
                    bottomSheetNavigator.show(DeepLinkDetailsScreen(it.id, showFolder))
                }
            },
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface),
        ) {
            BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

            AnimatedContent(
                targetState = detailsMode,
                label = "details_ui_anim",
            ) { target ->
                when (target) {
                    DetailsMode.Collapsed -> DeepLinkDetailsCollapsedUI(
                        modifier = Modifier,
                        showFolder = showFolder,
                        uiState = uiState,
                        onExpand = { detailsMode = DetailsMode.Expanded },
                        onFolderClicked = {
                            bottomSheetNavigator.hide()

                            val screen = ScreenRegistry.get(
                                SharedScreen.FolderDetails(
                                    uiState.deepLink.folder!!.id,
                                ),
                            )
                            bottomSheetNavigator.show(screen)
                        },
                    )

                    DetailsMode.Expanded -> DeepLinkDetailsExpandedUI(
                        modifier = Modifier,
                        uiState = uiState,
                        onNameChanged = screenModel::updateDeepLinkName,
                        onDescriptionChanged = screenModel::updateDeepLinkDescription,
                        onDeleteDeepLink = { showDeleteBottomSheet = true },
                        onAddFolder = screenModel::insertFolder,
                        onSelectFolder = screenModel::selectFolder,
                        onRemoveFolder = screenModel::removeFolderFromDeepLink,
                        onCollapse = { detailsMode = DetailsMode.Collapsed },
                    )

                    is DetailsMode.Duplicate -> DuplicateDeepLinkUI(
                        deepLink = uiState.deepLink,
                        onBack = {
                            detailsMode = (detailsMode as DetailsMode.Duplicate).lastMode
                        },
                        errorMessage = uiState.duplicateErrorMessage,
                        onDuplicate = screenModel::duplicate,
                    )
                }
            }

            AnimatedVisibility(
                visible = detailsMode !is DetailsMode.Duplicate,
            ) {
                Column {
                    Divider(modifier = Modifier.padding(top = 24.dp))

                    DeepLinkActionsRow(
                        link = uiState.deepLink.link,
                        isFavorite = uiState.deepLink.isFavorite,
                        onShare = screenModel::share,
                        onFavorite = screenModel::favorite,
                        onLaunch = screenModel::launch,
                        onDuplicate = { detailsMode = DetailsMode.Duplicate(detailsMode) },
                    )
                }
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    private sealed interface DetailsMode {
        data object Expanded : DetailsMode
        data object Collapsed : DetailsMode
        data class Duplicate(val lastMode: DetailsMode) : DetailsMode
    }

    companion object {
        private const val DELAY_BEFORE_SHOW_DUPLICATE = 500L
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
