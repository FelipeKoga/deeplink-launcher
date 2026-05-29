package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.FolderListItem
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun CreateFolderCardPreview() {
    DLLPreviewTheme {
        CreateFolderCard(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun FolderCardEmptyPreview() {
    DLLPreviewTheme {
        FolderCard(
            item = FolderListItem(
                folder = Folder(
                    id = "1",
                    name = "minha pasta 2",
                    description = null,
                    deepLinkCount = 0,
                ),
            ),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun FolderCardWithIconsPreview() {
    DLLPreviewTheme {
        FolderCard(
            item = FolderListItem(
                folder = Folder(
                    id = "2",
                    name = "minha pasta 1",
                    description = null,
                    deepLinkCount = 5,
                ),
                previewIcons = persistentListOf(null, null, null, null),
            ),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun FolderCardWithOverflowPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FolderCard(
                item = FolderListItem(
                    folder = Folder(
                        id = "3",
                        name = "work links",
                        description = null,
                        deepLinkCount = 6,
                    ),
                    previewIcons = persistentListOf(null, null, null, null),
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun FolderCardGridPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CreateFolderCard(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
            FolderCard(
                item = FolderListItem(
                    folder = Folder(
                        id = "4",
                        name = "favorites",
                        description = null,
                        deepLinkCount = 2,
                    ),
                    previewIcons = persistentListOf(null, null),
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
