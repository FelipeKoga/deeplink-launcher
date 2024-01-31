package dev.koga.deeplinklauncher.mapper

import dev.koga.deeplinklauncher.database.GetDeepLinkById
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.database.SelectAllDeeplinks
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.database.Deeplink as DatabaseDeepLink
import dev.koga.deeplinklauncher.model.DeepLink as DomainDeepLink

fun DatabaseDeepLink.toDomain() = DomainDeepLink(
    id = id,
    createdAt = createdAt,
    link = link,
    name = name,
    description = description,
    isFavorite = isFavorite == 1L,
    lastLaunchedAt = lastLaunchedAt,
)

fun SelectAllDeeplinks.toDomain() = DomainDeepLink(
    id = id,
    createdAt = createdAt,
    link = link,
    name = name,
    description = description,
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

fun GetDeepLinkById.toDomain() = DeepLink(
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

fun GetDeepLinkByLink.toDomain() = DeepLink(
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
