@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.datatransfer.impl.domain.dto

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

@Serializable
internal data class Payload(
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

internal fun Payload.Folder.toModel() = Folder(
    id = id,
    name = name,
    description = description,
)

internal fun Payload.DeepLink.toModel(folder: Folder?) = DeepLink(
    id = id ?: Uuid.random().toString(),
    createdAt = createdAt?.let { LocalDateTime.parse(it) } ?: Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault(),
    ),
    link = link,
    name = name,
    description = description,
    isFavorite = isFavorite ?: false,
    folder = folder,
)
