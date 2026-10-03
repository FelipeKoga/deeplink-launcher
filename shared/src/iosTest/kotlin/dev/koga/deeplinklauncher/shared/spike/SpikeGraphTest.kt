package dev.koga.deeplinklauncher.shared.spike

import androidx.lifecycle.SavedStateHandle
import dev.zacsweers.metro.createGraph
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SpikeGraphTest {
    @Test
    fun graphBindsEverything() {
        val graph = createGraph<IosSpikeGraph>()
        assertSame(graph.analyticsTracker, graph.analyticsTracker)
        assertSame(graph.appCoroutineScope, graph.appCoroutineScope)
        assertEquals("ios", graph.platformName.value)
        assertTrue(graph.analyticsTracker::class.simpleName == "NoOpAnalyticsTracker")
        val factory = graph.assistedFactoryProviders.getValue(SpikeViewModel::class)() as SpikeViewModel.Factory
        val viewModel = factory.create(SavedStateHandle(mapOf("id" to "42")))
        assertEquals("42", viewModel.savedStateHandle.get<String>("id"))
        assertSame(graph.analyticsTracker, viewModel.analyticsTracker)
    }
}
