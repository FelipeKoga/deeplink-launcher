package dev.koga.deeplinklauncher.model

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val id: String,
    val name: String,
    val description: String?,
    val deepLinkCount: Int,
)