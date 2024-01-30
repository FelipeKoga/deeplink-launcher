package dev.koga.deeplinklauncher.dto

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class ImportExportDeepLinkDto(
    val id: String?,
    val createdAt: String?,
    val link: String,
    val name: String?,
    val description: String?,
    val folder: ImportExportFolderDto?,
    val isFavorite: Boolean?,
)

@Serializable
data class ImportExportFolderDto(
    val id: String?,
    val name: String,
    val description: String?,
)

fun ImportExportDeepLinkDto.toDeepLink() = DeepLink(
    id = this.id ?: UUIDProvider.get(),
    createdAt = Clock.System.now(),
    link = this.link,
    name = this.name,
    description = this.description,
    isFavorite = this.isFavorite ?: false,
    folder = this.folder?.let { folder ->
        Folder(
            id = folder.id ?: UUIDProvider.get(),
            name = folder.name,
            description = folder.description,
            deepLinkCount = 0,
        )
    },
)

fun ImportExportFolderDto.toFolder() = Folder(
    id = id ?: UUIDProvider.get(),
    name = name,
    description = description,
)
