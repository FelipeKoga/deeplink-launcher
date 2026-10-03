package dev.koga.deeplinklauncher.shared

import dev.koga.deeplinklauncher.shared.di.DesktopAppGraph
import dev.zacsweers.metro.createGraph

actual object AppInitializer {
    fun init() {
        start(graph = createGraph<DesktopAppGraph>())
    }
}
