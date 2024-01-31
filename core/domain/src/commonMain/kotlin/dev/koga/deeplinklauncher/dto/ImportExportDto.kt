package dev.koga.deeplinklauncher.dto

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class ImportDto(
    val folders: List<Folder>? = null,
    val deepLinks: List<DeepLink>
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


fun ImportDto.Folder.toModel() = Folder(
    id = id,
    name = name,
    description = description,
)

fun ImportDto.DeepLink.toModel(folder: Folder?) = DeepLink(
    id = id ?: UUIDProvider.get(),
    createdAt = createdAt?.toInstant() ?: Clock.System.now(),
    link = link,
    name = name,
    description = description,
    isFavorite = isFavorite ?: false,
    folder = folder
)