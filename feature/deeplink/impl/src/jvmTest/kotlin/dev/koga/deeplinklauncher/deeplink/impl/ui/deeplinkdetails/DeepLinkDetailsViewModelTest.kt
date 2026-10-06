package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinkForDetails
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DeleteDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlers
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LinkDeepLinkToFolder
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.data.repository.RepositoryFixture
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.EditAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.navigation.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DeepLinkDetailsViewModelTest {
    private val fixture = RepositoryFixture()
    private val updatedShortcuts = mutableListOf<DeepLink>()
    private val viewModels = mutableListOf<DeepLinkDetailsViewModel>()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        runBlocking { viewModels.forEach { it.viewModelScope.coroutineContext.job.cancelAndJoin() } }
        Dispatchers.resetMain()
        fixture.close()
    }

    @Test
    fun anInvalidLinkIsReportedAndNotSaved() = runTest {
        val viewModel = editing()

        viewModel.onAction(EditAction.OnLinkChanged("my app://x"))
        advanceUntilIdle()
        viewModel.uiState.first { (it as? DeepLinkDetailsUiState.Edit)?.errorMessage == "Invalid deeplink" }

        assertEquals("myapp://a", fixture.deepLinks.getDeepLinkById("a")?.link)
        assertEquals(emptyList(), updatedShortcuts)
    }

    @Test
    fun aValidLinkIsSavedAndClearsTheError() = runTest {
        val viewModel = editing()

        viewModel.onAction(EditAction.OnLinkChanged("my app://x"))
        advanceUntilIdle()
        viewModel.onAction(EditAction.OnLinkChanged("myapp://x"))
        advanceUntilIdle()
        viewModel.uiState.first { (it as? DeepLinkDetailsUiState.Edit)?.deepLink?.link == "myapp://x" }

        assertEquals("myapp://x", fixture.deepLinks.getDeepLinkById("a")?.link)
        assertEquals(null, (viewModel.uiState.value as DeepLinkDetailsUiState.Edit).errorMessage)
        assertEquals(listOf("myapp://x"), updatedShortcuts.map(DeepLink::link))
    }

    private suspend fun TestScope.editing(): DeepLinkDetailsViewModel {
        val viewModel = viewModel()
        viewModel.onAction(LaunchAction.Edit)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        viewModel.uiState.first { it is DeepLinkDetailsUiState.Edit && it.deepLink.id == "a" }
        return viewModel
    }

    private fun viewModel() = DeepLinkDetailsViewModel(
        route = DeepLinkRouteEntryPoint.DeepLinkDetails(id = "a", showFolder = false),
        folderRepository = fixture.folders,
        deepLinkRepository = fixture.deepLinks,
        enrichDeepLinkForDetails = object : EnrichDeepLinkForDetails {
            override suspend fun invoke(deepLink: DeepLink) = error("not used in edit mode")
        },
        launchDeepLink = object : LaunchDeepLink {
            override suspend fun launch(url: String) = error("not used")
            override suspend fun launch(deepLink: DeepLink) = error("not used")
        },
        getDeepLinkHandlers = object : GetDeepLinkHandlers {
            override suspend fun invoke(link: String) = emptyList<Nothing>()
        },
        shareDeepLink = object : ShareDeepLink {
            override fun invoke(deepLink: DeepLink) = Unit
        },
        pinDeepLinkToHomeScreen = object : PinDeepLinkToHomeScreen {
            override fun invoke(deepLink: DeepLink) = PinDeepLinkToHomeScreen.Result.NotSupported
        },
        shortcutManager = object : DeepLinkShortcutManager {
            override suspend fun isAdded(deepLinkId: String) = false
            override suspend fun add(deepLink: DeepLink) = error("not used")
            override suspend fun update(deepLink: DeepLink) {
                updatedShortcuts += deepLink
            }
            override suspend fun remove(deepLinkId: String) = Unit
            override suspend fun enable(deepLinkIds: List<String>) = Unit
            override suspend fun disable(deepLinkIds: List<String>) = Unit
        },
        duplicateDeepLink = object : DuplicateDeepLink {
            override suspend fun invoke(deepLinkId: String, newLink: String, copyAllFields: Boolean) = error("not used")
        },
        deleteDeepLink = object : DeleteDeepLink {
            override suspend fun invoke(id: String) = Unit
        },
        linkDeepLinkToFolder = object : LinkDeepLinkToFolder {
            override suspend fun invoke(deepLinkId: String, folderId: String) = error("not used")
        },
        validateDeepLink = object : ValidateDeepLink {
            override fun isValid(link: String) = ' ' !in link && ':' in link
        },
        coroutineDebouncer = CoroutineDebouncer(),
        appNavigator = object : AppNavigator {
            override val commands: Flow<NavigationCommand> = emptyFlow()
            override fun navigate(route: AppRoute) = Unit
            override fun popBackStack() = Unit
        },
        analyticsTracker = object : AnalyticsTracker {
            override fun logEvent(name: String, parameters: Map<String, String>) = Unit
        },
    ).also(viewModels::add)
}
