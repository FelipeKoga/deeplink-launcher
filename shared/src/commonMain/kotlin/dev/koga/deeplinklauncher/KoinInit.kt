package dev.koga.deeplinklauncher

import dev.koga.deeplinklauncher.di.databaseModule
import dev.koga.deeplinklauncher.di.domainModule
import dev.koga.deeplinklauncher.module.featureExportScreenModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module = module {}) {
    startKoin {
        modules(
            appModule,
            domainModule,
            databaseModule,
        )
    }
}