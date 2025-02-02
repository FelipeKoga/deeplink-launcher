package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DeepLinkDetailsActions
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.DuplicateModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.EditModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.LaunchModeUI
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.event.DeepLinkDetailsEvent
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(
    private val deepLinkId: String,
    private val showFolder: Boolean,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val screenModel = getScreenModel<DeepLinkDetailsScreenModel>(
            parameters = { parametersOf(deepLinkId) },
        )
        val uiState by screenModel.uiState.collectAsState()

        var detailsMode by remember { mutableStateOf<DetailsMode>(DetailsMode.Launch) }
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
                bottomSheetNavigator.replace(
                    item = DeepLinkDetailsScreen(
                        deepLinkId = it.id,
                        showFolder = showFolder,
                    ),
                )
            },
        )

        SelectionContainer {
            Column {
                BottomSheetDefaults.DragHandle(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                DetailsTopBar(
                    onBack = { detailsMode = DetailsMode.Launch },
                    onDelete = { showDeleteBottomSheet = true },
                    mode = detailsMode
                )

                AnimatedContent(
                    targetState = detailsMode,
                    label = "details_ui_anim",
                ) { target ->
                    Column {
                        when (target) {
                            DetailsMode.Launch -> LaunchModeUI(
                                modifier = Modifier,
                                showFolder = showFolder,
                                uiState = uiState,
                                onFolderClicked = {
                                    bottomSheetNavigator.hide()
                                    navigator.push(
                                        ScreenRegistry.get(
                                            SharedScreen.FolderDetails(
                                                uiState.deepLink.folder!!.id,
                                            ),
                                        ),
                                    )
                                },
                            )

                            DetailsMode.Edit -> EditModeUI(
                                modifier = Modifier,
                                uiState = uiState,
                                onNameChanged = screenModel::updateName,
                                onDescriptionChanged = screenModel::updateDescription,
                                onLinkChanged = screenModel::updateLink,
                                onAddFolder = screenModel::insertFolder,
                                onToggleFolder = screenModel::toggleFolder,
                            )

                            is DetailsMode.Duplicate -> DuplicateModeUI(
                                deepLink = uiState.deepLink,
                                errorMessage = uiState.duplicateErrorMessage,
                                onDuplicate = screenModel::duplicate,
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = detailsMode is DetailsMode.Launch,
                ) {
                    Column {
                        DLLHorizontalDivider(
                            modifier = Modifier.padding(top = 24.dp),
                        )

                        DeepLinkDetailsActions(
                            isFavorite = uiState.deepLink.isFavorite,
                            onShare = screenModel::share,
                            onFavorite = screenModel::toggleFavorite,
                            onLaunch = screenModel::launch,
                            onDuplicate = { detailsMode = DetailsMode.Duplicate(detailsMode) },
                            onEdit = { detailsMode = DetailsMode.Edit }
                        )
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    mode: DetailsMode,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val showNavigationIcon by remember(mode) {
        derivedStateOf { mode.backTo != null }
    }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showNavigationIcon) {
            DLLIconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = TablerIcons.ArrowLeft,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (mode is DetailsMode.Edit) {
            DLLIconButton(
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = TablerIcons.Trash,
                    contentDescription = "Delete deeplink",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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

private sealed interface DetailsMode {
    val backTo: DetailsMode?

    data object Edit : DetailsMode {
        override val backTo: DetailsMode = Launch
    }

    data object Launch : DetailsMode {
        override val backTo: DetailsMode? = null
    }

    data class Duplicate(override val backTo: DetailsMode) : DetailsMode
}
