package dev.koga.deeplinklauncher.android

import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.shared.initKoin
import org.koin.android.ext.android.inject
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initKoin(
            appModule = module {
                single<Context> { this@App }
            },
        )

        val purchaseApi by inject<PurchaseApi>()
        purchaseApi.init()

        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
    }
}
