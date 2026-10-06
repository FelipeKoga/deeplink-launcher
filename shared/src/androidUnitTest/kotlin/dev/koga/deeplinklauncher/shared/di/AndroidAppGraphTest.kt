package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.createGraphFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AndroidAppGraphTest {

    @Test
    fun createsEveryViewModelFromTheAndroidGraph() {
        val graph = createGraphFactory<AndroidAppGraph.Factory>().create(RuntimeEnvironment.getApplication())

        assertSame(graph.appCoroutineScope, graph.appCoroutineScope)
        assertSame(graph.appDispatchers, graph.appDispatchers)
        assertSame(graph.coroutineDebouncer, graph.coroutineDebouncer)
        assertSame(graph.appNavigator, graph.appNavigator)
        assertSame(graph.appNavGraph, graph.appNavGraph)
        assertSame(graph.snackBarDispatcher, graph.snackBarDispatcher)
        assertSame(graph.analyticsTracker, graph.analyticsTracker)
        assertSame(graph.preferencesDataSource, graph.preferencesDataSource)
        assertSame(graph.purchaseApi, graph.purchaseApi)

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
            setOf("AddFolderViewModel"),
            graph.assistedFactoryProviders.keys.map { it.simpleName }.toSet(),
        )
        assertEquals(
            setOf(
                "DeepLinkDetailsViewModel.Factory",
                "FolderDetailsViewModel.Factory",
                "LinkDeepLinkForFolderViewModel.Factory",
            ),
            graph.manualAssistedFactoryProviders.keys.map { it.qualifiedName?.split('.')?.takeLast(2)?.joinToString(".") }.toSet(),
        )

        val created = graph.createViewModels(graph.viewModelProviders.keys + graph.assistedFactoryProviders.keys)
        assertEquals(created.map { it.first }, created.map { it.second })
    }
}
