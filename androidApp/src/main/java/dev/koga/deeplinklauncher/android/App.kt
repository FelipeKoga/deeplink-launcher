package dev.koga.deeplinklauncher.android

import android.app.Application
import dev.koga.deeplinklauncher.shared.AppInitializer

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInitializer.init(this)
    }
}
