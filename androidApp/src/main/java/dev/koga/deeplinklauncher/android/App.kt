package dev.koga.deeplinklauncher.android

import android.app.Application
import android.content.Context
import dev.koga.deeplinklauncher.shared.AppInitializer
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInitializer.init(
            appModule = module {
                single<Context> { this@App }
            },
        )
    }
}
