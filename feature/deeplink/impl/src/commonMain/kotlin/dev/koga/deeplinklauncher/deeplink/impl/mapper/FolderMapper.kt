package dev.koga.deeplinklauncher.deeplink.impl.mapper

import dev.koga.deeplinklauncher.database.GetFolderById
import dev.koga.deeplinklauncher.database.GetFolderDeepLinks
import dev.koga.deeplinklauncher.database.SelectFoldersWithDeeplinkCount
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder

fun SelectFoldersWithDeeplinkCount.toDomain() = Folder(
    id = id,
    name = name,
    description = description,
    deepLinkCount = deeplinkCount.toInt(),
)

fun GetFolderById.toDomain() = Folder(
    id = id,
    name = name,
    description = description,
    deepLinkCount = deeplinkCount.toInt(),
)

fun GetFolderDeepLinks.toDomain() = DeepLink(
    id = id,
    link = link,
    name = name,
    description = description,
    createdAt = createdAt,
    isFavorite = isFavorite == 1L,
    lastLaunchedAt = lastLaunchedAt,
    folder = folderId?.let { folderId ->
        Folder(
            id = folderId,
            name = name_.orEmpty(),
            description = description_,
        )
    },
)
