package dev.koga.deeplinklauncher.dto

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

@Serializable
data class ImportExportDto(
    val folders: List<Folder>? = null,
    val deepLinks: List<DeepLink>,
) {
    @Serializable
    data class DeepLink(
        val link: String,
        val id: String? = null,
        val createdAt: String? = null,
        val name: String? = null,
        val description: String? = null,
        val folderId: String? = null,
        val isFavorite: Boolean? = false,
    )

    @Serializable
    data class Folder(
        val id: String,
        val name: String,
        val description: String? = null,
    )
}

fun ImportExportDto.Folder.toModel() = Folder(
    id = id,
    name = name,
    description = description,
)

fun ImportExportDto.DeepLink.toModel(folder: Folder?) = DeepLink(
    id = id ?: UUIDProvider.get(),
    createdAt = createdAt?.toLocalDateTime() ?: Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault(),
    ),
    link = link,
    name = name,
    description = description,
    isFavorite = isFavorite ?: false,
    folder = folder,
)
