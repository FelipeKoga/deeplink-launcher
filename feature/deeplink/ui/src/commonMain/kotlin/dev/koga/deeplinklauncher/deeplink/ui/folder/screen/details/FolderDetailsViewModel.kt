package dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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

class FolderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val launchDeepLink: LaunchDeepLink,
) : ViewModel() {
    private val folderId = savedStateHandle.get<String>("folder_id").orEmpty()

    private val folder = repository.getFolderById(folderId)!!

    private val form = MutableStateFlow(
        folder.let {
            FolderDetailsUiState(
                name = it.name,
                description = it.description.orEmpty(),
                deepLinks = persistentListOf(),
            )
        },
    )

    private val deepLinks = repository.getFolderDeepLinksStream(folderId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val uiState = combine(
        form,
        deepLinks,
    ) { form, deepLinks ->
        form.copy(
            deepLinks = deepLinks.toPersistentList(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = form.value,
    )

    private val deleteDispatcher = Channel<Unit>()
    val deletedEvent = deleteDispatcher.receiveAsFlow()

    init {
        form.onEach {
            repository.upsertFolder(
                folder.copy(
                    name = it.name,
                    description = it.description,
                ),
            )
        }.launchIn(viewModelScope)
    }

    fun delete() {
        repository.deleteFolder(folderId)
        viewModelScope.launch { deleteDispatcher.send(Unit) }
    }

    fun updateName(value: String) {
        form.update { it.copy(name = value) }
    }

    fun updateDescription(value: String) {
        form.update { it.copy(description = value) }
    }

    fun launch(deepLink: DeepLink) {
        viewModelScope.launch {
            launchDeepLink.launch(deepLink)
        }
    }
}


