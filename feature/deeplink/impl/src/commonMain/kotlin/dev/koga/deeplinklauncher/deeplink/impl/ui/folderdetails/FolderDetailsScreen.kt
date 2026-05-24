package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component.DeleteFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component.EditableText
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsUiState
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkCard
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.designsystem.utils.fullLineItem
import dev.koga.deeplinklauncher.designsystem.utils.spacer
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.ui.calculateWindowSizeSharedClass

@Composable
internal fun FolderDetailsScreen(
    viewModel: FolderDetailsViewModel,
    appNavigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteFolderBottomSheet(
            onDismissRequest = { showDeleteDialog = false },
            onDelete = {
                showDeleteDialog = false
                viewModel.onAction(FolderDetailsAction.Delete)
            },
        )
    }

    FolderDetailsUI(
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigate = appNavigator::navigate,
        onShowDeleteConfirmation = { showDeleteDialog = true },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FolderDetailsUI(
    uiState: FolderDetailsUiState,
    onAction: (FolderDetailsAction) -> Unit,
    onNavigate: (AppRoute) -> Unit,
    onShowDeleteConfirmation: () -> Unit,
) {
    val colors = DeepLinkTheme.colors

    Scaffold(
        containerColor = colors.surface.background,
        topBar = {
            DLLTopBar(
                title = {},
                navigationIcon = {
                    DLLTopBarDefaults.NavigationIcon(
                        onClicked = { onNavigate(AppRoute.PopBackStack) },
                    )
                },
                actions = {
                    DLLIconButton(
                        onClick = onShowDeleteConfirmation,
                    ) {
                        Icon(
                            imageVector = TablerIcons.Trash,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        FolderDetailsScreenContent(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            uiState = uiState,
            onAction = onAction,
            onNavigate = onNavigate,
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun FolderDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: FolderDetailsUiState,
    onAction: (FolderDetailsAction) -> Unit,
    onNavigate: (DeepLinkRouteEntryPoint) -> Unit,
) {
    val dimensions = DeepLinkTheme.dimensions
    val typography = DeepLinkTheme.typography

    val windowSizeClass = calculateWindowSizeSharedClass()

    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(numberOfColumns),
        state = rememberLazyStaggeredGridState(),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
    ) {
        spacer(height = 24.dp)

        fullLineItem {
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = "Name",
                    style = typography.label.caption,
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditableText(
                    value = uiState.name,
                    onSave = { onAction(FolderDetailsAction.UpdateName(it)) },
                    inputLabel = "Enter a name",
                    editButtonEnabled = uiState.name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = uiState.name,
                        style = typography.title.page,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Description",
                    style = typography.label.caption,
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditableText(
                    value = uiState.description,
                    onSave = { onAction(FolderDetailsAction.UpdateDescription(it)) },
                    inputLabel = "Enter a description",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = uiState.description.ifEmpty { "--" },
                        style = typography.body.default,
                    )
                }
            }
        }

        fullLineItem {
            DLLHorizontalDivider(
                modifier = Modifier.padding(vertical = dimensions.extraLarge),
            )
        }

        fullLineItem {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = if (uiState.deepLinks.isNotEmpty()) {
                        "Deeplinks"
                    } else {
                        "No Deeplinks vinculated to this folder"
                    },
                    style = typography.label.caption,
                )

                DLLIconButton(
                    onClick = { onAction(FolderDetailsAction.OpenLinkDeepLinkScreen) },
                ) {
                    Icon(
                        imageVector = TablerIcons.Plus,
                        contentDescription = "Link deeplink",
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        if (uiState.deepLinks.isEmpty()) {
            fullLineItem {
                DLLButton(
                    onClick = { onAction(FolderDetailsAction.OpenLinkDeepLinkScreen) },
                    text = "Link deeplinks",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                )
            }
        }

        items(
            count = uiState.deepLinks.size,
            key = { uiState.deepLinks[it].deepLink.id },
        ) { index ->
            val item = uiState.deepLinks[index]
            val deepLink = item.deepLink

            DeepLinkCard(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .animateItem(),
                item = item,
                onClick = {
                    onNavigate(
                        DeepLinkRouteEntryPoint.DeepLinkDetails(id = deepLink.id, showFolder = false),
                    )
                },
                onLaunch = { onAction(FolderDetailsAction.Launch(deepLink)) },
                showFolder = false,
            )
        }

        spacer(height = 24.dp)
    }
}
