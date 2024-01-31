package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenRegistry
import dev.koga.deeplinklauncher.di.databaseModule
import dev.koga.deeplinklauncher.di.deepLinkDetailsModule
import dev.koga.deeplinklauncher.di.domainModule
import dev.koga.deeplinklauncher.di.exportModule
import dev.koga.deeplinklauncher.di.folderDetailsModule
import dev.koga.deeplinklauncher.di.importModule
import dev.koga.deeplinklauncher.di.settingsModule
import dev.koga.deeplinklauncher.navigation.deepLinkDetailsScreenModule
import dev.koga.deeplinklauncher.navigation.exportScreenModule
import dev.koga.deeplinklauncher.navigation.folderDetailsScreenModule
import dev.koga.deeplinklauncher.navigation.homeScreenModule
import dev.koga.deeplinklauncher.navigation.importScreenModule
import dev.koga.deeplinklauncher.navigation.settingsScreenModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module = module {}) {
    ScreenRegistry {
        exportScreenModule()
        importScreenModule()
        deepLinkDetailsScreenModule()
        folderDetailsScreenModule()
        settingsScreenModule()
    }

    startKoin {
        modules(
            appModule,
            homeScreenModule,
            folderDetailsModule,
            deepLinkDetailsModule,
            settingsModule,
            exportModule,
            importModule,
            domainModule,
            databaseModule,
        )
    }
}
