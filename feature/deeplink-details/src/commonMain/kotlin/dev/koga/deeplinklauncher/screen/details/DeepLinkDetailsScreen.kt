package dev.koga.deeplinklauncher.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.DLLNavigationIcon
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.button.DLLIconButton
import dev.koga.deeplinklauncher.button.DLLOutlinedIconButton
import dev.koga.deeplinklauncher.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkDetailsBottomBar
import dev.koga.deeplinklauncher.screen.details.component.DuplicateModeUI
import dev.koga.deeplinklauncher.screen.details.component.EditModeUI
import dev.koga.deeplinklauncher.screen.details.component.LaunchModeUI
import dev.koga.deeplinklauncher.screen.details.event.DeepLinkDetailsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(
    private val deepLinkId: String,
    private val showFolder: Boolean,
) : Screen {

    override val key: ScreenKey = "deeplink_details_$deepLinkId"

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
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                DetailsTopBar(
                    mode = detailsMode,
                    onDelete = { showDeleteBottomSheet = true },
                    changeDetailsTo = { detailsMode = it },
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
                                onSelectFolder = screenModel::selectFolder,
                                onRemoveFolder = screenModel::removeFolderFromDeepLink,
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

                        DeepLinkDetailsBottomBar(
                            link = uiState.deepLink.link,
                            isFavorite = uiState.deepLink.isFavorite,
                            onShare = screenModel::share,
                            onFavorite = screenModel::toggleFavorite,
                            onLaunch = screenModel::launch,
                            onDuplicate = { detailsMode = DetailsMode.Duplicate(detailsMode) },
                        )
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    mode: DetailsMode,
    onDelete: () -> Unit,
    changeDetailsTo: (DetailsMode) -> Unit,
) {
    val showNavigationIcon by remember(mode) {
        derivedStateOf { mode.backTo != null }
    }

    DLLTopBar(
        modifier = modifier,
        title = if (mode is DetailsMode.Duplicate) "Duplicate DeepLink" else "",
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            if (showNavigationIcon) {
                DLLNavigationIcon(onClicked = {
                    changeDetailsTo(mode.backTo!!)
                })
            }
        },
        actions = {
            when (mode) {
                DetailsMode.Launch -> {
                    DLLIconButton(
                        onClick = onDelete,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete deeplink",
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    DLLOutlinedIconButton(
                        modifier = Modifier,
                        onClick = { changeDetailsTo(DetailsMode.Edit) },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit deeplink",
                        )
                    }
                }

                DetailsMode.Edit -> {
                    DLLIconButton(
                        onClick = onDelete,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete deeplink",
                        )
                    }
                }

                is DetailsMode.Duplicate -> Unit
            }
        },
    )
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
