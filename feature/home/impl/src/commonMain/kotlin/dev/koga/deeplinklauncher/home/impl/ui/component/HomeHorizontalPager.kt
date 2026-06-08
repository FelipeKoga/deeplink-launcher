package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.api.ui.model.FolderListItem
import dev.koga.deeplinklauncher.deeplink.uicomponent.CreateFolderCard
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkCard
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkCardActionsPresets
import dev.koga.deeplinklauncher.deeplink.uicomponent.FolderCard
import dev.koga.deeplinklauncher.ui.calculateWindowSizeSharedClass
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DeepLinksLazyColumn(
    modifier: Modifier = Modifier,
    listState: LazyGridState,
    deepLinks: List<DeepLinkListItem>,
    contentPadding: PaddingValues,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onToggleFavorite: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
) {
    val padding = PaddingValues(
        start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
        end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
        top = contentPadding.calculateTopPadding() + 12.dp,
        bottom = contentPadding.calculateBottomPadding() + 12.dp,
    )

    HomeVerticalGridList(
        modifier = modifier,
        state = listState,
        contentPadding = padding,
    ) {
        items(
            count = deepLinks.size,
            key = { deepLinks[it].deepLink.id },
        ) { index ->
            val item = deepLinks[index]
            val deepLink = item.deepLink

            DeepLinkCard(
                modifier = Modifier.animateItem(),
                item = item,
                onClick = { onClick(deepLink) },
                actions = DeepLinkCardActionsPresets.browse(
                    onLaunch = { onLaunch(deepLink) },
                    onToggleFavorite = { onToggleFavorite(deepLink) },
                ),
                onFolderClicked = { onFolderClicked(deepLink.folder!!) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeVerticalGridList(
    modifier: Modifier = Modifier,
    state: LazyGridState,
    contentPadding: PaddingValues,
    content: LazyGridScope.() -> Unit,
) {
    val windowSizeClass = calculateWindowSizeSharedClass()

    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns),
        state = state,
        modifier = modifier.fillMaxSize().padding(horizontal = 12.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FoldersVerticalStaggeredGrid(
    modifier: Modifier = Modifier,
    folders: ImmutableList<FolderListItem>,
    contentPadding: PaddingValues,
    onAdd: () -> Unit,
    onClick: (Folder) -> Unit,
) {
    val padding = PaddingValues(
        start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
        end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
        top = contentPadding.calculateTopPadding() + 12.dp,
        bottom = contentPadding.calculateBottomPadding() + 12.dp,
    )

    val windowSizeClass = calculateWindowSizeSharedClass()
    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 3
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns),
        state = rememberLazyGridState(),
        modifier = modifier.fillMaxSize().padding(horizontal = 12.dp),
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            CreateFolderCard(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
            )
        }

        items(folders.size, key = { folders[it].folder.id }) { index ->
            FolderCard(
                item = folders[index],
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
            )
        }
    }
}
