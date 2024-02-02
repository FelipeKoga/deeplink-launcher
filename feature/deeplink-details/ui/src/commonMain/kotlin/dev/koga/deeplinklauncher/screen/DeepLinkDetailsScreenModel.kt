package dev.koga.deeplinklauncher.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.ShareDeepLink
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DeepLinkDetailScreenModel(
    deepLinkId: String,
    private val folderDataSource: FolderDataSource,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
) : ScreenModel {

    private val deepLink = deepLinkDataSource.getDeepLinkById(deepLinkId)!!
    private val folders = folderDataSource.getFoldersStream().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )
    private val form = MutableStateFlow(
        DeepLinkForm(
            name = deepLink.name.orEmpty(),
            description = deepLink.description.orEmpty(),
            folder = deepLink.folder,
            link = deepLink.link,
            isFavorite = deepLink.isFavorite,
            deleted = false
        ),
    )

    val uiState = combine(
        folders,
        form,
    ) { folders, form ->
        DeepLinkDetailsUiState(
            folders = folders.toPersistentList(),
            form = form,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DeepLinkDetailsUiState(
            folders = persistentListOf(),
            form = form.value,
        ),
    )

    init {
        form.onEach {
            if (it.deleted) {
                deepLinkDataSource.deleteDeepLink(deepLinkId)
                return@onEach
            }

            deepLinkDataSource.upsertDeepLink(
                deepLink.copy(
                    name = it.name.ifEmpty { null },
                    description = it.description.ifEmpty { null },
                    folder = it.folder,
                    isFavorite = it.isFavorite,
                ),
            )
        }.launchIn(screenModelScope)
    }

    fun updateDeepLinkName(s: String) {
        form.update { it.copy(name = s) }
    }

    fun updateDeepLinkDescription(s: String) {
        form.update { it.copy(description = s) }
    }

    fun favorite() {
        form.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun launch() {
        launchDeepLink.launch(deepLink)
    }

    fun delete() {
        form.update { it.copy(deleted = true) }
    }

    fun share() {
        shareDeepLink(deepLink)
    }

    fun insertFolder(name: String, description: String) {
        val folder = Folder(
            id = UUIDProvider.get(),
            name = name,
            description = description,
        )

        folderDataSource.upsertFolder(folder)

        selectFolder(folder)
    }

    fun selectFolder(folder: Folder) {
        form.update { it.copy(folder = folder) }
    }

    fun removeFolderFromDeepLink() {
        form.update { it.copy(folder = null) }
    }
}