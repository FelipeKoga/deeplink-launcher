package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.lifecycle.SavedStateHandle
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.navigation.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AddFolderViewModelTest {
    private val sentCommands = mutableListOf<NavigationCommand>()
    private val events = mutableListOf<String>()
    private val navigator = object : AppNavigator {
        override val commands: Flow<NavigationCommand> = emptyFlow()
        override fun navigate(route: AppRoute) {
            sentCommands += NavigationCommand.Navigate(route)
        }

        override fun popBackStack() {
            sentCommands += NavigationCommand.Back
        }
    }
    private val analytics = object : AnalyticsTracker {
        override fun logEvent(name: String, parameters: Map<String, String>) {
            events += name
        }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun keepsSheetOpenWhenNameIsTaken() = runTest {
        val viewModel = AddFolderViewModel(SavedStateHandle(mapOf("name" to "Work")), TakenNames, navigator, analytics)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.add()

        assertEquals("A folder with this name already exists", viewModel.uiState.value.errorMessage)
        assertEquals(emptyList(), sentCommands)
        assertEquals(emptyList(), events)

        viewModel.onNameChanged("Work 2")
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    private object TakenNames : FolderRepository {
        override fun upsertFolder(folder: Folder) = FolderRepository.UpsertResult.NameAlreadyExists("existing")
        override fun getFoldersStream(): Flow<List<Folder>> = emptyFlow()
        override fun getFolders(): List<Folder> = emptyList()
        override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> = emptyFlow()
        override fun getFolderByIdStream(id: String): Flow<Folder?> = emptyFlow()
        override fun getFolderById(id: String): Folder? = null
        override fun deleteFolder(id: String) = Unit
        override fun deleteAll() = Unit
    }
}
