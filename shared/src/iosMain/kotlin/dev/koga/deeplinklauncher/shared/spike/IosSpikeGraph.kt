package dev.koga.deeplinklauncher.shared.spike

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
internal interface IosSpikeGraph : SpikeGraph
