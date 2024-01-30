package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.usecase.folder.DeleteFolder
import dev.koga.deeplinklauncher.usecase.folder.GetFolderById
import dev.koga.deeplinklauncher.usecase.folder.GetFolderDeepLinksStream
import dev.koga.deeplinklauncher.usecase.folder.UpsertFolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FolderDetailsScreenModel(
    private val folderId: String,
    private val deleteFolder: DeleteFolder,
    getFolderById: GetFolderById,
    getFolderDeepLinksStream: GetFolderDeepLinksStream,
    upsertFolder: UpsertFolder,
) : ScreenModel {

    private val folder = getFolderById(folderId)!!

    private val form = MutableStateFlow(
        folder.let {
            FolderDetails(
                name = it.name,
                description = it.description.orEmpty(),
                deepLinks = emptyList(),
            )
        },
    )

    private val deepLinks = getFolderDeepLinksStream(folderId).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val state = combine(
        form,
        deepLinks,
    ) { form, deepLinks ->
        form.copy(
            deepLinks = deepLinks,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = form.value,
    )

    private val deleteDispatcher = Channel<Unit>()
    val deletedEvent = deleteDispatcher.receiveAsFlow()

    init {
        form.onEach {
            upsertFolder(
                folder.copy(
                    name = it.name,
                    description = it.description,
                ),
            )
        }.launchIn(screenModelScope)
    }

    fun delete() {
        deleteFolder(folderId)
        screenModelScope.launch { deleteDispatcher.send(Unit) }
    }

    fun updateName(value: String) {
        form.update { it.copy(name = value) }
    }

    fun updateDescription(value: String) {
        form.update { it.copy(description = value) }
    }
}

data class FolderDetails(
    val name: String,
    val description: String,
    val deepLinks: List<DeepLink>,
)
