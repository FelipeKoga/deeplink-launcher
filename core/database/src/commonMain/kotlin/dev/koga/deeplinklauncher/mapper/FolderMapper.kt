package dev.koga.deeplinklauncher.mapper

import dev.koga.deeplinklauncher.database.GetFolderById
import dev.koga.deeplinklauncher.database.SelectFoldersWithDeeplinkCount
import dev.koga.deeplinklauncher.model.Folder

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
