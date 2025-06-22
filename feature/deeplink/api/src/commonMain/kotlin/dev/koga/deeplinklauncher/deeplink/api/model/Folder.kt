package dev.koga.deeplinklauncher.deeplink.api.model

import kotlinx.serialization.Serializable

@Serializable
public data class Folder(
    val id: String,
    val name: String,
    val description: String?,
    val deepLinkCount: Int = 0,
)
