package dev.koga.deeplinklauncher.shared

import dev.koga.deeplinklauncher.coroutines.di.coroutinesModule
import dev.koga.deeplinklauncher.database.di.databaseModule
import dev.koga.deeplinklauncher.datatransfer.impl.di.dataTransferModule
import dev.koga.deeplinklauncher.deeplink.impl.di.deepLinkModule
import dev.koga.deeplinklauncher.file.di.fileModule
import dev.koga.deeplinklauncher.home.di.homeModule
import dev.koga.deeplinklauncher.navigation.di.navigationModule
import dev.koga.deeplinklauncher.preferences.di.preferencesModule
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.impl.di.purchaseModule
import dev.koga.deeplinklauncher.settings.impl.di.settingsModule
import dev.koga.deeplinklauncher.uievent.di.uiEventModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

object AppInitializer {

    fun init() {
        init(appModule = module {  })
    }

    fun init(appModule: Module) {
        val koin = startKoin {
            modules(
                appModule,
                deepLinkModule,
                preferencesModule,
                purchaseModule,
                dataTransferModule,
                homeModule,
                navigationModule,
                settingsModule,
                fileModule,
                databaseModule,
                platformModule,
                coroutinesModule,
                uiEventModule,
            )
        }.koin

        val purchaseApi = koin.get<PurchaseApi>()
        purchaseApi.init()
    }
}