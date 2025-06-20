package dev.koga.deeplinklauncher.shared

import dev.koga.deeplinklauncher.coroutines.di.coroutinesModule
import dev.koga.deeplinklauncher.database.di.databaseModule
import dev.koga.deeplinklauncher.deeplink.impl.di.deeplinkImplModule
import dev.koga.deeplinklauncher.deeplink.ui.di.deeplinkUiModule
import dev.koga.deeplinklauncher.file.di.fileModule
import dev.koga.deeplinklauncher.home.ui.di.homeUiModule
import dev.koga.deeplinklauncher.importdata.ui.di.importExportUiModule
import dev.koga.deeplinklauncher.importexport.impl.di.importExportImplModule
import dev.koga.deeplinklauncher.navigation.di.navigationModule
import dev.koga.deeplinklauncher.preferences.di.preferencesModule
import dev.koga.deeplinklauncher.purchase.impl.di.purchaseImplModule
import dev.koga.deeplinklauncher.settings.ui.di.settingsUiModule
import dev.koga.deeplinklauncher.uievent.di.uiEventModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin() {
    initKoin(module {})
}

fun initKoin(appModule: Module) {
    startKoin {
        modules(
            appModule,
            deeplinkUiModule,
            deeplinkImplModule,
            preferencesModule,
            purchaseImplModule,
            importExportUiModule,
            importExportImplModule,
            homeUiModule,
            navigationModule,
            settingsUiModule,
            fileModule,
            databaseModule,
            purchaseImplModule,
            platformModule,
            coroutinesModule,
            uiEventModule,
        )
    }
}
