package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.di.databaseModule
import dev.koga.deeplinklauncher.di.deepLinkDetailsUiModule
import dev.koga.deeplinklauncher.di.domainModule
import dev.koga.deeplinklauncher.di.exportDeepLinksDomainModule
import dev.koga.deeplinklauncher.di.exportDeepLinksUiModule
import dev.koga.deeplinklauncher.di.folderDetailsUiModule
import dev.koga.deeplinklauncher.di.homeDomainModule
import dev.koga.deeplinklauncher.di.importDomainModule
import dev.koga.deeplinklauncher.di.importUiModule
import dev.koga.deeplinklauncher.di.settingsDomainModule
import dev.koga.deeplinklauncher.di.settingsUiModule
import dev.koga.deeplinklauncher.di.homeUiModule
import dev.koga.deeplinklauncher.screen.ExportScreen
import dev.koga.deeplinklauncher.screen.ImportScreen
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

private val screenModule = screenModule {
    register<SharedScreen.ImportDeepLinks> { ImportScreen() }
    register<SharedScreen.ExportDeepLinks> { ExportScreen() }
    register<SharedScreen.Settings> { SettingsScreen() }
    register<SharedScreen.DeepLinkDetails> { provider -> DeepLinkDetailsScreen(provider.id) }
    register<SharedScreen.FolderDetails> { provider -> FolderDetailsScreen(provider.id) }
}

fun initKoin(appModule: Module = module {}) {
    ScreenRegistry { screenModule() }

    startKoin {
        modules(
            homeUiModule,
            homeDomainModule,
            folderDetailsUiModule,
            deepLinkDetailsUiModule,
            settingsDomainModule,
            settingsUiModule,
            importUiModule,
            importDomainModule,
            exportDeepLinksUiModule,
            exportDeepLinksDomainModule,
        )

        modules(
            appModule,
            domainModule,
            databaseModule,
        )
    }
}

