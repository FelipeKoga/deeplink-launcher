package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class AddFolderViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val name = savedStateHandle.getStateFlow("name", "")
    private val description = savedStateHandle.getStateFlow("description", "")

    val uiState = combine(
        name,
        description,
    ) { name, description ->
        AddFolderUiState(
            name = name,
            description = description,
            isSubmitEnabled = name.isNotBlank(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddFolderUiState(
            name = name.value,
            description = description.value,
            isSubmitEnabled = false,
        ),
    )

    fun onNameChanged(text: String) {
        savedStateHandle["name"] = text
    }

    fun onDescriptionChanged(text: String) {
        savedStateHandle["description"] = text
    }

    fun add() {
        val folder = Folder(
            id = Uuid.random().toString(),
            name = name.value,
            description = description.value,
        )

        repository.upsertFolder(folder)
        appNavigator.popBackStack()
    }
}
