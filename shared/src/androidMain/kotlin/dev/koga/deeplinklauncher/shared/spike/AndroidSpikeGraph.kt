package dev.koga.deeplinklauncher.shared.spike

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidSpikeGraph : SpikeGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AndroidSpikeGraph
    }
}
