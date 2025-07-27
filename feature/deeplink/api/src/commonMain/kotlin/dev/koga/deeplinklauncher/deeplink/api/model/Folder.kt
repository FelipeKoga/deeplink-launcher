package dev.koga.deeplinklauncher.deeplink.api.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
public data class Folder(
    val id: String,
    val name: String,
    val description: String?,
    val deepLinkCount: Int = 0,
) {
    public companion object {
        @OptIn(ExperimentalUuidApi::class)
        public val preview: Folder = Folder(
            id = Uuid.random().toString(),
            name = "Folder name",
            description = "Folder description",
        )

        @OptIn(ExperimentalUuidApi::class)
        public val previewOneDeepLinkCount: Folder = Folder(
            id = Uuid.random().toString(),
            name = "Folder name",
            description = "Folder description",
            deepLinkCount = 1,
        )
    }
}
