package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.createGraph
import kotlin.test.Test
import kotlin.test.assertSame

class DesktopAppGraphTest {
    private val graph = createGraph<DesktopAppGraph>()

    @Test
    fun coroutineBindingsAreSingletons() {
        assertSame(graph.appCoroutineScope, graph.appCoroutineScope)
        assertSame(graph.appDispatchers, graph.appDispatchers)
        assertSame(graph.coroutineDebouncer, graph.coroutineDebouncer)
    }
}
