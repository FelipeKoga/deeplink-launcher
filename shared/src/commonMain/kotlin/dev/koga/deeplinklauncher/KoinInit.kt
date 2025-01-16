package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.database.di.databaseModule
import dev.koga.deeplinklauncher.deeplink.impl.di.deeplinkImplModule
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsScreen
import dev.koga.deeplinklauncher.deeplink.ui.di.deeplinkUiModule
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsScreen
import dev.koga.deeplinklauncher.file.di.fileModule
import dev.koga.deeplinklauncher.home.ui.di.homeUiModule
import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.importexport.impl.di.importExportImplModule
import dev.koga.deeplinklauncher.preferences.impl.di.preferencesImplModule
import dev.koga.deeplinklauncher.purchase.impl.di.purchaseImplModule
import dev.koga.deeplinklauncher.settings.ui.di.settingsUiModule
import dev.koga.deeplinklauncher.settings.ui.screen.SettingsScreen
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

private val screenModule = screenModule {
    register<SharedScreen.ImportDeepLinks> { ImportScreen() }
    register<SharedScreen.ExportDeepLinks> { ExportScreen() }
    register<SharedScreen.Settings> { SettingsScreen() }
    register<SharedScreen.DeepLinkDetails> { provider ->
        DeepLinkDetailsScreen(
            provider.id,
            provider.showFolder,
        )
    }
    register<SharedScreen.FolderDetails> { provider -> FolderDetailsScreen(provider.id) }
}

fun initKoin() {
    initKoin(module {})
}

fun initKoin(appModule: Module) {
    ScreenRegistry { screenModule() }

    startKoin {
        modules(
            appModule,
            deeplinkUiModule,
            deeplinkImplModule,
            preferencesImplModule,
            purchaseImplModule,
            importExportImplModule,
            homeUiModule,
            settingsUiModule,
            fileModule,
            databaseModule,
            purchaseImplModule,
        )
    }
}
