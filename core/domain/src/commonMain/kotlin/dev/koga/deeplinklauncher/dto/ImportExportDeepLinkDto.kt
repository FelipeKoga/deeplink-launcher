package dev.koga.deeplinklauncher.dto

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class ImportExportDeepLinkDto(
    val link: String,
    val id: String? = null,
    val createdAt: String? = null,
    val name: String? = null,
    val description: String? = null,
    val folder: ImportExportFolderDto? = null,
    val isFavorite: Boolean? = false,
)

@Serializable
data class ImportExportFolderDto(
    val id: String,
    val name: String,
    val description: String? = null,
)

fun ImportExportDeepLinkDto.toDeepLink() = DeepLink(
    id = this.id ?: UUIDProvider.get(),
    createdAt = createdAt?.toInstant() ?: Clock.System.now(),
    link = this.link,
    name = this.name,
    description = this.description,
    isFavorite = this.isFavorite ?: false,
    folder = this.folder?.toFolder()
)

fun ImportExportFolderDto.toFolder() = Folder(
    id = id,
    name = name,
    description = description,
)
