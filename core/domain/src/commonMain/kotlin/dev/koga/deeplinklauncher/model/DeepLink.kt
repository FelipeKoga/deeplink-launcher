package dev.koga.deeplinklauncher.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DeepLink(
    val id: String,
    val link: String,
    val name: String?,
    val description: String?,
    val createdAt: LocalDateTime,
    val isFavorite: Boolean,
    val folder: Folder? = null,
) {
    val hasNameAndDescription: Boolean
        get() = !name.isNullOrBlank() && !description.isNullOrBlank()
}
