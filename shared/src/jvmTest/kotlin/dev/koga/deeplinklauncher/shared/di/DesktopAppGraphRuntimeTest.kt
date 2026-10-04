package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.createGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@OptIn(ExperimentalCoroutinesApi::class)
class DesktopAppGraphRuntimeTest {
    private val home = Files.createTempDirectory("dll-graph-test").toFile()
    private val originalHome = System.getProperty("user.home")

    @BeforeTest
    fun setUp() {
        System.setProperty("user.home", home.absolutePath)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        System.setProperty("user.home", originalHome)
        home.deleteRecursively()
    }

    @Test
    fun createsEveryViewModelAgainstARealDatabaseAndDataStore() {
        val graph = createGraph<DesktopAppGraph>()

        assertSame(graph.preferencesDataSource, graph.preferencesDataSource)
        assertSame(graph.appNavGraph, graph.appNavGraph)

        val created = graph.createViewModels(graph.viewModelProviders.keys + graph.assistedFactoryProviders.keys)
        assertEquals(created.map { it.first }, created.map { it.second })
    }
}
