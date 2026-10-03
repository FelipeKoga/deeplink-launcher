package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.createGraph
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class IosAppGraphTest {
    private val graph = createGraph<IosAppGraph>()

    @Test
    fun appScopedBindingsAreSingletons() {
        assertSame(graph.appCoroutineScope, graph.appCoroutineScope)
        assertSame(graph.appDispatchers, graph.appDispatchers)
        assertSame(graph.coroutineDebouncer, graph.coroutineDebouncer)
        assertSame(graph.appNavigator, graph.appNavigator)
        assertSame(graph.snackBarDispatcher, graph.snackBarDispatcher)
        assertSame(graph.analyticsTracker, graph.analyticsTracker)
        assertSame(graph.purchaseApi, graph.purchaseApi)
    }

    @Test
    fun everyViewModelIsContributed() {
        assertEquals(
            setOf(
                "AppThemeViewModel",
                "DeleteDataViewModel",
                "ExportViewModel",
                "HomeViewModel",
                "ImportViewModel",
                "ProductsViewModel",
                "SettingsViewModel",
                "SuggestionsOptionViewModel",
            ),
            graph.viewModelProviders.keys.map { it.simpleName }.toSet(),
        )
        assertEquals(
            setOf(
                "AddFolderViewModel",
                "DeepLinkDetailsViewModel",
                "FolderDetailsViewModel",
                "LinkDeepLinkForFolderViewModel",
            ),
            graph.assistedFactoryProviders.keys.map { it.simpleName }.toSet(),
        )
    }
}
