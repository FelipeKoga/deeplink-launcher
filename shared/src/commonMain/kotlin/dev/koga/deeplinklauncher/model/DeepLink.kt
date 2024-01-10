package dev.koga.deeplinklauncher.model

data class DeepLink(
    val id: String,
    val link: String,
    val name: String?,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long?,
    val folder: Folder?
)