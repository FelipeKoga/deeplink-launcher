package dev.koga.deeplinklauncher.android

import android.app.Application
import android.content.Context
import dev.koga.deeplinklauncher.android.di.screenModule
import dev.koga.deeplinklauncher.di.managerModule
import dev.koga.deeplinklauncher.di.useCaseModule
import dev.koga.deeplinklauncher.initKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            appModule = module {
                single<Context> { this@App }
                includes(screenModule, managerModule, useCaseModule)
            }
        )
    }
}