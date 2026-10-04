package dev.koga.deeplinklauncher.shared

import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import com.skydoves.compose.stability.runtime.RecompositionEvent
import com.skydoves.compose.stability.runtime.RecompositionLogger
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

    ComposeStabilityAnalyzer.setLogger(object : RecompositionLogger {
        override fun log(event: RecompositionEvent) {
            println("################ RECOMPOSITION: $${event.tag} - ${event.composableName} - ${event.recompositionCount} - ${event.unstableParameters}")

//                if (event.recompositionCount >= 10) {
//                    // Example: Send to Firebase Analytics
//                    FirebaseAnalytics.getInstance(this).logEvent("excessive_recomposition") {
//                        param("tag", event.tag)
//                        param("composable", event.composableName)
//                        param("count", event.recompositionCount)
//                        param("unstable_params", event.unstableParameters.joinToString())
//                    }
//                }
        }
    })
}
