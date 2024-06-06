package dev.koga.deeplinklauncher.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.DLLNavigationIcon
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.button.DLLIconButton
import dev.koga.deeplinklauncher.button.DLLOutlinedIconButton
import dev.koga.deeplinklauncher.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.hideWithResult
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkActionsRow
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkDetailsCollapsedUI
import dev.koga.deeplinklauncher.screen.details.component.DeepLinkDetailsExpandedUI
import dev.koga.deeplinklauncher.screen.details.component.DuplicateDeepLinkUI
import dev.koga.deeplinklauncher.screen.details.event.DeepLinkDetailsEvent
import dev.koga.resources.Res
import dev.koga.resources.ic_unfold_more_24dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(
    private val deepLinkId: String,
    private val showFolder: Boolean,
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
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
                bottomSheetNavigator.hideWithResult(
                    key = SharedScreen.DeepLinkDetails.Result.KEY,
                    result = it.id,
                )
            },
        )

        SelectionContainer {
            Column {
                BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

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
                            DetailsMode.Collapsed -> DeepLinkDetailsCollapsedUI(
                                modifier = Modifier,
                                showFolder = showFolder,
                                uiState = uiState,
                                onFolderClicked = {
                                    bottomSheetNavigator.hideWithResult(
                                        key = SharedScreen.DeepLinkDetails.Result.KEY,
                                        result = SharedScreen
                                            .DeepLinkDetails
                                            .Result
                                            .NavigateToFolderDetails(uiState.deepLink.folder!!.id),
                                    )
                                },
                            )

                            DetailsMode.Expanded -> DeepLinkDetailsExpandedUI(
                                modifier = Modifier,
                                uiState = uiState,
                                onNameChanged = screenModel::updateName,
                                onDescriptionChanged = screenModel::updateDescription,
                                onAddFolder = screenModel::insertFolder,
                                onSelectFolder = screenModel::selectFolder,
                                onRemoveFolder = screenModel::removeFolderFromDeepLink,
                            )

                            is DetailsMode.Duplicate -> DuplicateDeepLinkUI(
                                deepLink = uiState.deepLink,
                                errorMessage = uiState.duplicateErrorMessage,
                                onDuplicate = screenModel::duplicate,
                            )
                        }
                    }

                }

                AnimatedVisibility(
                    visible = detailsMode !is DetailsMode.Duplicate,
                ) {
                    Column {
                        DLLHorizontalDivider(
                            modifier = Modifier.padding(top = 24.dp),
                        )

                        DeepLinkActionsRow(
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
            containerColor = Color.Transparent
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
                DetailsMode.Collapsed -> {
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
                        onClick = { changeDetailsTo(DetailsMode.Expanded) },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit deeplink",
                        )
                    }
                }

                DetailsMode.Expanded -> {
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
        }
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

    data object Expanded : DetailsMode {
        override val backTo: DetailsMode = Collapsed
    }

    data object Collapsed : DetailsMode {
        override val backTo: DetailsMode? = null
    }

    data class Duplicate(override val backTo: DetailsMode) : DetailsMode
}
