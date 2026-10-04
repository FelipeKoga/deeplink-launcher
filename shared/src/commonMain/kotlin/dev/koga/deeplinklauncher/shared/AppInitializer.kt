package dev.koga.deeplinklauncher.shared

import dev.koga.deeplinklauncher.shared.analytics.AppOpen
import dev.koga.deeplinklauncher.shared.analytics.track
import dev.koga.deeplinklauncher.shared.di.AppGraph

expect object AppInitializer

internal lateinit var appGraph: AppGraph
    private set

internal fun AppInitializer.start(graph: AppGraph) {
    appGraph = graph
    graph.purchaseApi.init()
    graph.analyticsTracker.track(AppOpen)
}
