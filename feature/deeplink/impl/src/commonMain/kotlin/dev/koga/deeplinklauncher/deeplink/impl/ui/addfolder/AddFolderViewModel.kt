package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.impl.analytics.FolderCreated
import dev.koga.deeplinklauncher.deeplink.impl.analytics.track
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.state.AddFolderUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@AssistedInject
internal class AddFolderViewModel(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val appNavigator: AppNavigator,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    @AssistedFactory
    @ViewModelAssistedFactoryKey(AddFolderViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): AddFolderViewModel = create(extras.createSavedStateHandle())

        fun create(@Assisted savedStateHandle: SavedStateHandle): AddFolderViewModel
    }

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
