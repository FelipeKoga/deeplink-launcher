package dev.koga.deeplinklauncher.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinksLazyColumnPreview() {
    DLLPreviewTheme {
        DeepLinksLazyColumn(
            listState = rememberLazyGridState(),
            deepLinks = listOf(
                previewFavorite,
                previewNotFavorite,
                previewNotFavorite,
            ),
            contentPadding = PaddingValues(0.dp),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinksLazyColumnEmptyPreview() {
    DLLPreviewTheme {
        DeepLinksLazyColumn(
            listState = rememberLazyGridState(),
            deepLinks = listOf(),
            contentPadding = PaddingValues(0.dp),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FoldersVerticalStaggeredGridPreview() {
    DLLPreviewTheme {
        FoldersVerticalStaggeredGrid(
            folders = persistentListOf(
                Folder.preview,
                Folder.preview,
                Folder.preview,
            ),
            contentPadding = PaddingValues(0.dp),
            onAdd = {},
            onClick = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FoldersVerticalStaggeredGridEmptyPreview() {
    DLLPreviewTheme {
        FoldersVerticalStaggeredGrid(
            folders = persistentListOf(),
            contentPadding = PaddingValues(0.dp),
            onAdd = {},
            onClick = {},
        )
    }
}
