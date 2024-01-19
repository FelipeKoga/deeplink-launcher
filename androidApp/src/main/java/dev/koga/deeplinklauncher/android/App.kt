package dev.koga.deeplinklauncher.android

import android.app.Application
import dev.koga.deeplinklauncher.android.di.screenModule
import dev.koga.deeplinklauncher.di.databaseModule
import dev.koga.deeplinklauncher.di.managerModule
import dev.koga.deeplinklauncher.di.repositoryModule
import dev.koga.deeplinklauncher.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        val modules =
            databaseModule + screenModule + repositoryModule + useCaseModule + managerModule

        startKoin {
            androidContext(this@App)
            modules(modules)
        }
    }
}