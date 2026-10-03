package dev.koga.deeplinklauncher.shared.spike

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface DesktopSpikeGraph : SpikeGraph
