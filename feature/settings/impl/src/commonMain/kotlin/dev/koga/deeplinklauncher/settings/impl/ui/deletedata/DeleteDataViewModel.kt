package dev.koga.deeplinklauncher.settings.impl.ui.deletedata

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DeleteAllDeepLinks
import dev.koga.deeplinklauncher.settings.impl.analytics.DataDeleted
import dev.koga.deeplinklauncher.settings.impl.analytics.track
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch

enum class DeletionType {
    ALL,
    DEEP_LINKS,
    FOLDERS,
}

@ViewModelKey
@ContributesIntoMap(AppScope::class)
class DeleteDataViewModel(
    private val deleteAllDeepLinks: DeleteAllDeepLinks,
    private val folderRepository: FolderRepository,
    private val appCoroutineScope: AppCoroutineScope,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    fun delete(type: DeletionType) {
        analyticsTracker.track(DataDeleted(deletionType = type.name.lowercase()))
        appCoroutineScope.launch {
            when (type) {
                DeletionType.ALL -> {
                    deleteAllDeepLinks()
                    folderRepository.deleteAll()
                }
                DeletionType.DEEP_LINKS -> deleteAllDeepLinks()
                DeletionType.FOLDERS -> folderRepository.deleteAll()
            }
        }
    }
}
