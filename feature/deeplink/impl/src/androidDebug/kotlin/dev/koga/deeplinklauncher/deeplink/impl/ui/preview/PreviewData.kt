package dev.koga.deeplinklauncher.deeplink.impl.ui.preview

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal val previewFavoriteDeepLink: DeepLink = DeepLink(
    id = "1",
    link = "https://example.com",
    name = "Example",
    description = "Example description",
    isFavorite = true,
)

@OptIn(ExperimentalUuidApi::class)
internal val previewNotFavoriteDeepLink: DeepLink = DeepLink(
    id = "2",
    link = "https://example.com",
    name = "Example",
    description = "Example description",
    isFavorite = false,
)

@OptIn(ExperimentalUuidApi::class)
internal val previewFolder: Folder = Folder(
    id = Uuid.random().toString(),
    name = "Folder name",
    description = "Folder description",
)

@OptIn(ExperimentalUuidApi::class)
internal val previewFolderOneDeepLinkCount: Folder = Folder(
    id = Uuid.random().toString(),
    name = "Folder name",
    description = "Folder description",
    deepLinkCount = 1,
)
