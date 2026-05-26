package dev.koga.deeplinklauncher.shared

import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import com.skydoves.compose.stability.runtime.RecompositionEvent
import com.skydoves.compose.stability.runtime.RecompositionLogger
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.analytics.impl.di.analyticsModule
import dev.koga.deeplinklauncher.coroutines.di.coroutinesModule
import dev.koga.deeplinklauncher.database.di.databaseModule
import dev.koga.deeplinklauncher.datatransfer.impl.di.dataTransferModule
import dev.koga.deeplinklauncher.deeplink.impl.di.deepLinkModule
import dev.koga.deeplinklauncher.file.di.fileModule
import dev.koga.deeplinklauncher.home.impl.di.homeModule
import dev.koga.deeplinklauncher.navigation.di.navigationModule
import dev.koga.deeplinklauncher.preferences.di.preferencesModule
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.impl.di.purchaseModule
import dev.koga.deeplinklauncher.settings.impl.di.settingsModule
import dev.koga.deeplinklauncher.shared.analytics.AppOpen
import dev.koga.deeplinklauncher.shared.analytics.track
import dev.koga.deeplinklauncher.uievent.di.uiEventModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

object AppInitializer {

    fun init() {
        init(appModule = module { })
    }

    fun init(appModule: Module) {
        val koin = startKoin {
            modules(
                appModule,
                analyticsModule,
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

        val analyticsTracker = koin.get<AnalyticsTracker>()
        analyticsTracker.track(AppOpen)

        ComposeStabilityAnalyzer.setLogger(object : RecompositionLogger {
            override fun log(event: RecompositionEvent) {
                println("################ RECOMPOSITION: $${event.tag} - ${event.composableName} - ${event.recompositionCount} - ${event.unstableParameters}")

//                if (event.recompositionCount >= 10) {
//                    // Example: Send to Firebase Analytics
//                    FirebaseAnalytics.getInstance(this).logEvent("excessive_recomposition") {
//                        param("tag", event.tag)
//                        param("composable", event.composableName)
//                        param("count", event.recompositionCount)
//                        param("unstable_params", event.unstableParameters.joinToString())
//                    }
//                }
            }
        })
    }
}
