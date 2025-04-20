package dev.koga.deeplinklauncher.home.ui.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.component.DeepLinkCard
import dev.koga.deeplinklauncher.deeplink.ui.folder.component.FolderCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DeepLinksLazyColumn(
    modifier: Modifier = Modifier,
    listState: LazyGridState,
    deepLinks: List<DeepLink>,
    contentPadding: PaddingValues,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
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
            key = { deepLinks[it].id },
        ) { index ->
            val deepLink = deepLinks[index]

            DeepLinkCard(
                modifier = Modifier.animateItem(),
                deepLink = deepLink,
                onClick = { onClick(deepLink) },
                onLaunch = { onLaunch(deepLink) },
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
    val windowSizeClass = calculateWindowSizeClass()

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
    folders: ImmutableList<Folder>,
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

    val windowSizeClass = calculateWindowSizeClass()
    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 3
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns),
        state = rememberLazyGridState(),
        modifier = modifier.fillMaxSize().padding(horizontal = 12.dp),
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            OutlinedCard(
                onClick = onAdd,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(184.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = TablerIcons.Plus,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Create new folder",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }

        items(folders.size, key = { folders[it].id }) { index ->
            FolderCard(
                folder = folders[index],
                onClick = { onClick(it) },
                modifier = Modifier.animateItem(),
            )
        }
    }
}
