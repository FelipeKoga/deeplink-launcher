package dev.koga.deeplinklauncher.deeplink.common.model

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
public data class DeepLink(
    val id: String,
    val link: String,
    val name: String?,
    val description: String?,
    val createdAt: LocalDateTime = currentLocalDateTime,
    val isFavorite: Boolean,
    val lastLaunchedAt: LocalDateTime? = null,
    val folder: Folder? = null,
) {
    public companion object {
        public val empty: DeepLink = DeepLink(
            id = "",
            link = "",
            name = "",
            description = "",
            isFavorite = false,
        )
    }
}
