package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component.LinkDeepLinkToFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state.LinkDeepLinkForFolderAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state.LinkDeepLinkForFolderUiState
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkCard
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkCardActionsPresets
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkLaunchBottomBar
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute

@Composable
internal fun LinkDeepLinkForFolderScreen(
    viewModel: LinkDeepLinkForFolderViewModel,
    appNavigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.pendingLinkConfirmation?.let {
        LinkDeepLinkToFolderBottomSheet(
            folderName = uiState.folderName,
            onDismissRequest = { viewModel.onAction(LinkDeepLinkForFolderAction.DismissLinkConfirmation) },
            onConfirm = { viewModel.onAction(LinkDeepLinkForFolderAction.ConfirmLinkToFolder) },
        )
    }

    LinkDeepLinkForFolderUI(
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigate = appNavigator::navigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
internal fun LinkDeepLinkForFolderUI(
    uiState: LinkDeepLinkForFolderUiState,
    onAction: (LinkDeepLinkForFolderAction) -> Unit,
    onNavigate: (AppRoute) -> Unit,
    hazeState: HazeState = remember { HazeState() },
) {
    val colors = DeepLinkTheme.colors
    val shapes = DeepLinkTheme.shapes

    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(),
        ),
        containerColor = colors.surface.background,
        sheetPeekHeight = 80.dp,
        topBar = {
            DLLTopBar(
                title = {
                    DLLTopBarDefaults.Title(text = "Link deeplink")
                },
                navigationIcon = {
                    DLLTopBarDefaults.NavigationIcon(
                        onClicked = { onNavigate(AppRoute.PopBackStack) },
                    )
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(
                        containerColor = colors.surface.background,
                    ),
                ),
            )
        },
        sheetContainerColor = colors.surface.elevated,
        sheetContent = {
            DeepLinkLaunchBottomBar(
                modifier = Modifier
                    .clip(shapes.sheet)
                    .navigationBarsPadding(),
                state = uiState.deepLinkInputState,
                launch = { onAction(LinkDeepLinkForFolderAction.LaunchInputDeepLink) },
                onSuggestionClicked = { onAction(LinkDeepLinkForFolderAction.OnSuggestionClicked(it)) },
                onValueChange = { onAction(LinkDeepLinkForFolderAction.OnInputChanged(it)) },
            )
        },
    ) { contentPadding ->
        LinkDeepLinkForFolderContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .hazeSource(hazeState),
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
internal fun LinkDeepLinkForFolderContent(
    modifier: Modifier = Modifier,
    uiState: LinkDeepLinkForFolderUiState,
    onAction: (LinkDeepLinkForFolderAction) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val dimensions = DeepLinkTheme.dimensions

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = dimensions.mediumLarge),
    ) {
        item {
            Text(
                text = "Launch a new deeplink or select an existing one to add to \"${uiState.folderName}\".",
                style = typography.body.default.copy(color = colors.text.secondary),
                modifier = Modifier.padding(
                    horizontal = dimensions.extraLarge,
                    vertical = dimensions.mediumLarge,
                ),
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.medium))
        }

        item {
            DLLTextField(
                value = uiState.query,
                onValueChange = { onAction(LinkDeepLinkForFolderAction.QueryChanged(it)) },
                label = "Search deeplinks",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.extraLarge),
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.mediumLarge))
        }

        item {
            DLLHorizontalDivider()
        }

        if (uiState.linkableDeepLinks.isEmpty()) {
            item {
                Text(
                    text = "No deeplinks available to link.",
                    style = typography.body.default.copy(color = colors.text.secondary),
                    modifier = Modifier.padding(dimensions.extraLarge),
                )
            }
        } else {
            items(
                items = uiState.linkableDeepLinks,
                key = { it.deepLink.id },
            ) { item ->
                DeepLinkCard(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp),
                    item = item,
                    onClick = {
                        onAction(LinkDeepLinkForFolderAction.DeepLinkSelected(item.deepLink.id))
                    },
                    actions = DeepLinkCardActionsPresets.linkPicker,
                )
            }
        }
    }
}
