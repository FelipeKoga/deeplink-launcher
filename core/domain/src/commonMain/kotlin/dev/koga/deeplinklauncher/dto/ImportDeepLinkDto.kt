package dev.koga.deeplinklauncher.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImportDeepLinkDto(
    val id: String?,
    val createdAt: String?,
    val link: String,
    val name: String?,
    val description: String?,
    val folder: ImportFolderDto?,
    val isFavorite: Boolean?,
)

@Serializable
data class ImportFolderDto(
    val id: String?,
    val name: String,
    val description: String?,
)
