package dev.koga.deeplinklauncher.android

import android.app.Application
import com.point.android.feature.home.di.homeModule
import dev.koga.deeplinklauncher.di.databaseModule
import dev.koga.deeplinklauncher.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        val modules = databaseModule + homeModule + repositoryModule

        startKoin {
            androidContext(this@App)
            modules(modules)
        }
    }
}