package dev.koga.deeplinklauncher.screen

import dev.koga.deeplinklauncher.model.Folder
import kotlinx.collections.immutable.ImmutableList

data class DeepLinkDetailsUiState(
    val folders: ImmutableList<Folder>,
    val form: DeepLinkForm,
)

data class DeepLinkForm(
    val name: String,
    val description: String,
    val folder: Folder?,
    val link: String,
    val isFavorite: Boolean,
    val deleted: Boolean,
)
