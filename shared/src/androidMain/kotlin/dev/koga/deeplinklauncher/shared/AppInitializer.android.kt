package dev.koga.deeplinklauncher.shared

import android.content.Context
import dev.koga.deeplinklauncher.shared.di.AndroidAppGraph
import dev.zacsweers.metro.createGraphFactory

actual object AppInitializer {
    fun init(context: Context) {
        start(graph = createGraphFactory<AndroidAppGraph.Factory>().create(context))
    }
}
