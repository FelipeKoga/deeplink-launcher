package dev.koga.deeplinklauncher.settings.impl.deletedata

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository

enum class DeletionType {
    ALL,
    DEEP_LINKS,
    FOLDERS,
}

class DeleteDataViewModel(
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : ViewModel() {

    fun delete(type: DeletionType) {
        when (type) {
            DeletionType.ALL -> {
                deepLinkRepository.deleteAll()
                folderRepository.deleteAll()
            }
            DeletionType.DEEP_LINKS -> {
                deepLinkRepository.deleteAll()
            }
            DeletionType.FOLDERS -> {
                folderRepository.deleteAll()
            }
        }
    }
}
