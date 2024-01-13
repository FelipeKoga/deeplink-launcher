package dev.koga.deeplinklauncher.android.folder.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.FolderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FolderDetailsScreenModel(
    private val folderId: String,
    private val folderRepository: FolderRepository,
) : ScreenModel {

    private val folder = folderRepository.getFolderById(folderId)
    private val deepLinks = folderRepository.getFolderDeepLinks(folderId)

    val state = combine(
        folder,
        deepLinks,
    ) { folder, deepLinks ->
        FolderDetailsScreenState.Loaded(
            folder = folder,
            deepLinks = deepLinks
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FolderDetailsScreenState.Loading
    )

}

sealed interface FolderDetailsScreenState {
    data object Loading : FolderDetailsScreenState

    data class Loaded(val folder: Folder, val deepLinks: List<DeepLink>) : FolderDetailsScreenState
}