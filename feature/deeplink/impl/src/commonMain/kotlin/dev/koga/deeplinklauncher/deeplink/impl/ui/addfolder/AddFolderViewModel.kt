package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.impl.analytics.FolderCreated
import dev.koga.deeplinklauncher.deeplink.impl.analytics.track
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.state.AddFolderUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class AddFolderViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val appNavigator: AppNavigator,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {
    private val name = savedStateHandle.getStateFlow("name", "")
    private val description = savedStateHandle.getStateFlow("description", "")
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState = combine(
        name,
        description,
        errorMessage,
    ) { name, description, errorMessage ->
        AddFolderUiState(
            name = name,
            description = description,
            isSubmitEnabled = name.isNotBlank(),
            errorMessage = errorMessage,
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
        errorMessage.update { null }
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

        when (repository.upsertFolder(folder)) {
            FolderRepository.UpsertResult.Saved -> {
                analyticsTracker.track(FolderCreated)
                appNavigator.popBackStack()
            }

            is FolderRepository.UpsertResult.NameAlreadyExists -> {
                errorMessage.update { "A folder with this name already exists" }
            }
        }
    }
}
