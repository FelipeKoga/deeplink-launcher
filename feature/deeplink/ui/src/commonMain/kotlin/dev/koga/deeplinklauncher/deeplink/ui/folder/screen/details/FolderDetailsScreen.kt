package dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.Trash
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.component.DeepLinkCard
import dev.koga.deeplinklauncher.deeplink.ui.folder.component.DeleteFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.ui.folder.component.EditableText
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.LocalDimensions
import dev.koga.deeplinklauncher.designsystem.utils.fullLineItem
import dev.koga.deeplinklauncher.designsystem.utils.spacer

@Composable
fun FolderDetailsScreen(
    viewModel: FolderDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FolderDetailsUI(
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailsUI(
    uiState: FolderDetailsUiState,

) {
//    LaunchedEffect(Unit) {
//        screenModel.deletedEvent.collectLatest { navigator.pop() }
//    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteFolderBottomSheet(
            onDismissRequest = { showDeleteDialog = false },
            onDelete = {
                showDeleteDialog = false
//                screenModel.delete()
            },
        )
    }

    Scaffold(
        topBar = {
            DLLTopBar(
                title = {},
                navigationIcon = {
//                    DLLTopBarDefaults.navigationIcon(onClicked = navigator::pop)
                },
                actions = {
                    DLLIconButton(
                        onClick = { showDeleteDialog = true },
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

//        FolderDetailsScreenContent(
//            modifier = Modifier.fillMaxSize().padding(contentPadding),
//            form = uiState,
//            onEditName = screenModel::updateName,
//            onEditDescription = screenModel::updateDescription,
//            onDeepLinkClick = { deepLink ->
//                val screen = ScreenRegistry.get(
//                    SharedScreen.DeepLinkDetails(deepLink.id, false),
//                )
//                bottomSheetNavigator.show(screen)
//            },
//            onDeepLinkLaunch = screenModel::launch,
//        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FolderDetailsScreenContent(
    modifier: Modifier = Modifier,
    form: FolderDetailsUiState,
    onEditName: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    onDeepLinkClick: (DeepLink) -> Unit,
    onDeepLinkLaunch: (DeepLink) -> Unit,
) {
    val dimensions = LocalDimensions.current

    val windowSizeClass = calculateWindowSizeClass()

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
                    style = MaterialTheme.typography.labelSmall,
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditableText(
                    value = form.name,
                    onSave = onEditName,
                    inputLabel = "Enter a name",
                    editButtonEnabled = form.name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = form.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.labelSmall,
                )

                Spacer(modifier = Modifier.height(8.dp))

                EditableText(
                    value = form.description,
                    onSave = onEditDescription,
                    inputLabel = "Enter a description",
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = form.description.ifEmpty { "--" },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                    )
                }
            }
        }

        fullLineItem {
            DLLHorizontalDivider(
                modifier = Modifier.padding(vertical = dimensions.extraLarge),
                thickness = .4.dp,
            )
        }

        fullLineItem {
            Text(
                text = if (form.deepLinks.isNotEmpty()) {
                    "Deeplinks"
                } else {
                    "No Deeplinks vinculated to this folder"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        }

        items(
            count = form.deepLinks.size,
            key = { form.deepLinks[it].id },
        ) { index ->
            val deepLink = form.deepLinks[index]

            DeepLinkCard(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .animateItem(),
                deepLink = deepLink,
                onClick = { onDeepLinkClick(deepLink) },
                onLaunch = { onDeepLinkLaunch(deepLink) },
                showFolder = false,
            )
        }

        spacer(height = 24.dp)
    }
}
