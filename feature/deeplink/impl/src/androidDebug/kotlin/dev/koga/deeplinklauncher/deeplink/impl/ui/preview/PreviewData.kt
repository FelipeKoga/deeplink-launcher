package dev.koga.deeplinklauncher.deeplink.impl.ui.preview

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime

internal val previewNow: LocalDateTime = LocalDateTime(2026, 1, 15, 12, 30)

internal val previewFavoriteDeepLink: DeepLink = DeepLink(
    id = "1",
    link = "https://example.com",
    name = "Example",
    description = "Example description",
    createdAt = LocalDateTime(2026, 1, 15, 10, 30),
    isFavorite = true,
)

internal val previewFolder: Folder = Folder(
    id = "folder-1",
    name = "Folder name",
    description = "Folder description",
)

internal val previewFolderOneDeepLinkCount: Folder = Folder(
    id = "folder-2",
    name = "Folder name",
    description = "Folder description",
    deepLinkCount = 1,
)

internal val previewTargetAppHandlers = listOf(
    DeepLinkHandler(packageName = "com.example.dev", appName = "Example Dev"),
    DeepLinkHandler(packageName = "com.example.staging", appName = "Example Staging"),
    DeepLinkHandler(packageName = "com.example", appName = "Example"),
)

internal val previewDeepLinkDetails: DeepLinkDetailsModel = DeepLinkDetailsModel(
    deepLink = previewFavoriteDeepLink,
    metadata = DeepLinkMetadata(
        scheme = "https",
        host = "google.com",
        path = "/",
        query = null,
    ),
    handlerInfo = DeepLinkHandlerInfo.Available(
        canResolve = true,
        appName = "Chrome",
    ),
)

internal val previewDeepLinkListItems: ImmutableList<DeepLinkListItem> = persistentListOf(
    DeepLinkListItem(
        deepLink = previewFavoriteDeepLink,
    ),
    DeepLinkListItem(
        deepLink = DeepLink(
            id = "2",
            link = "myapp://orders/42?tab=details",
            name = null,
            description = null,
            createdAt = LocalDateTime(2026, 1, 10, 9, 0),
            isFavorite = false,
            lastLaunchedAt = LocalDateTime(2026, 1, 15, 12, 25),
        ),
    ),
    DeepLinkListItem(
        deepLink = DeepLink(
            id = "3",
            link = "https://example.com/profile/settings",
            name = "Profile settings",
            description = "Opens the profile settings page",
            createdAt = LocalDateTime(2025, 12, 1, 8, 0),
            isFavorite = true,
            folder = previewFolder,
        ),
    ),
)
