package dev.koga.deeplinklauncher.deeplink.api.model

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

expect val String.isLinkValid: Boolean
