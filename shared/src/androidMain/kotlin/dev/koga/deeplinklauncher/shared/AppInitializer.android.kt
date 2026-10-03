package dev.koga.deeplinklauncher.shared

import android.content.Context
import dev.koga.deeplinklauncher.shared.di.AndroidAppGraph
import dev.zacsweers.metro.createGraph
import org.koin.dsl.module

actual object AppInitializer {
    fun init(context: Context) {
        start(
            graph = createGraph<AndroidAppGraph>(),
            appModule = module { single<Context> { context } },
        )
    }
}
