package dev.koga.deeplinklauncher.deeplink.api.model

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

        public val previewFavorite: DeepLink = DeepLink(
            id = "1",
            link = "https://example.com",
            name = "Example",
            description = "Example description",
            isFavorite = true,
        )

        public val previewNotFavorite: DeepLink = DeepLink(
            id = "2",
            link = "https://example.com",
            name = "Example",
            description = "Example description",
            isFavorite = false,
        )
    }
}
