package dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import kotlinx.collections.immutable.ImmutableList
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

class FolderDetailsScreenModel(
    private val folderId: String,
    private val repository: FolderRepository,
    private val launchDeepLink: LaunchDeepLink,
) : ScreenModel {

    private val folder = repository.getFolderById(folderId)!!

    private val form = MutableStateFlow(
        folder.let {
            FolderDetails(
                name = it.name,
                description = it.description.orEmpty(),
                deepLinks = persistentListOf(),
            )
        },
    )

    private val deepLinks = repository.getFolderDeepLinksStream(folderId).stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val state = combine(
        form,
        deepLinks,
    ) { form, deepLinks ->
        form.copy(
            deepLinks = deepLinks.toPersistentList(),
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
            repository.upsertFolder(
                folder.copy(
                    name = it.name,
                    description = it.description,
                ),
            )
        }.launchIn(screenModelScope)
    }

    fun delete() {
        repository.deleteFolder(folderId)
        screenModelScope.launch { deleteDispatcher.send(Unit) }
    }

    fun updateName(value: String) {
        form.update { it.copy(name = value) }
    }

    fun updateDescription(value: String) {
        form.update { it.copy(description = value) }
    }

    fun launch(deepLink: DeepLink) {
        screenModelScope.launch {
            launchDeepLink.launch(deepLink)
        }
    }
}

data class FolderDetails(
    val name: String,
    val description: String,
    val deepLinks: ImmutableList<DeepLink>,
)
