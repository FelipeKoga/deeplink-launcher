package dev.koga.deeplinklauncher.android.folder.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.FolderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class FolderDetailsScreenModel(
    private val folderId: String,
    private val folderRepository: FolderRepository,
) : ScreenModel {

    private val folder = folderRepository.getFolderById(folderId)
    private val deepLinks = folderRepository.getFolderDeepLinks(folderId)
    private val deleted = MutableStateFlow(false)

    val state = combine(
        deepLinks,
        deleted,
    ) { deepLinks, deleted ->
        FolderDetailsScreenState.Loaded(
            folder = folder,
            deepLinks = deepLinks,
            deleted = deleted
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FolderDetailsScreenState.Loading
    )

    fun deleteFolder() {
        folderRepository.deleteFolderById(folderId)
        deleted.update { true }
    }


}

sealed interface FolderDetailsScreenState {
    data object Loading : FolderDetailsScreenState

    data class Loaded(val folder: Folder, val deepLinks: List<DeepLink>, val deleted: Boolean) : FolderDetailsScreenState
}