package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DeepLink(
    val id: String,
    val link: String,
    val name: String?,
    val description: String?,
    val createdAt: LocalDateTime = currentLocalDateTime,
    val isFavorite: Boolean,
    val lastLaunchedAt: LocalDateTime? = null,
    val folder: Folder? = null,
) {
    val hasNameAndDescription: Boolean
        get() = !name.isNullOrBlank() && !description.isNullOrBlank()

    companion object {
        val empty = DeepLink(
            id = "",
            link = "",
            name = "",
            description = "",
            isFavorite = false,
        )
    }
}
