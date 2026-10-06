package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsAction
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.navigation.NavigationCommand
import dev.koga.deeplinklauncher.uievent.SnackBar
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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
class FolderDetailsViewModelTest {
    private val folders = linkedMapOf(
        "f" to Folder(id = "f", name = "Work", description = "Mine"),
        "g" to Folder(id = "g", name = "Personal", description = null),
    )
    private val snackBars = mutableListOf<SnackBar>()
    private var folderStream: (String) -> Flow<Folder?> = { flowOf(folders[it]) }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun rejectedRenameDoesNotBreakLaterDescriptionSave() = runTest {
        val viewModel = viewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.onAction(FolderDetailsAction.UpdateName("Personal"))
        viewModel.onAction(FolderDetailsAction.UpdateDescription("Updated"))

        assertEquals(Folder(id = "f", name = "Work", description = "Updated"), folders["f"])
        assertEquals("Work", viewModel.uiState.value.name)
        assertEquals(listOf(SnackBar.Variant.ERROR), snackBars.map { it.variant })
    }

    @Test
    fun ignoresSavesBeforeTheFolderLoads() = runTest {
        folderStream = { MutableSharedFlow() }
        val viewModel = viewModel()

        viewModel.onAction(FolderDetailsAction.UpdateDescription("Early"))

        assertEquals(Folder(id = "f", name = "Work", description = "Mine"), folders["f"])
    }

    private fun viewModel() = FolderDetailsViewModel(
        route = DeepLinkRouteEntryPoint.FolderDetails(id = "f"),
        repository = repository,
        enrichDeepLinksForList = object : EnrichDeepLinksForList {
            override suspend fun invoke(links: List<DeepLink>): List<DeepLinkListItem> = emptyList()
        },
        launchDeepLink = object : LaunchDeepLink {
            override suspend fun launch(url: String) = throw UnsupportedOperationException()
            override suspend fun launch(deepLink: DeepLink) = throw UnsupportedOperationException()
        },
        appNavigator = object : AppNavigator {
            override val commands: Flow<NavigationCommand> = emptyFlow()
            override fun navigate(route: AppRoute) = Unit
            override fun popBackStack() = Unit
        },
        analyticsTracker = object : AnalyticsTracker {
            override fun logEvent(name: String, parameters: Map<String, String>) = Unit
        },
        snackBarDispatcher = object : SnackBarDispatcher {
            override val messages: Flow<SnackBar> = emptyFlow()
            override fun show(message: SnackBar) {
                snackBars += message
            }
            override fun show(message: String) {
                snackBars += SnackBar(message)
            }
        },
    )

    private val repository = object : FolderRepository {
        override fun upsertFolder(folder: Folder): FolderRepository.UpsertResult {
            val other = folders.values.firstOrNull { it.name == folder.name && it.id != folder.id }
            if (other != null) return FolderRepository.UpsertResult.NameAlreadyExists(other.id)
            folders[folder.id] = folder
            return FolderRepository.UpsertResult.Saved
        }
        override fun getFolderByIdStream(id: String): Flow<Folder?> = folderStream(id)
        override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> = flowOf(emptyList())
        override fun getFoldersStream(): Flow<List<Folder>> = emptyFlow()
        override fun getFolders(): List<Folder> = folders.values.toList()
        override fun getFolderById(id: String): Folder? = folders[id]
        override fun deleteFolder(id: String) = Unit
        override fun deleteAll() = Unit
    }
}
