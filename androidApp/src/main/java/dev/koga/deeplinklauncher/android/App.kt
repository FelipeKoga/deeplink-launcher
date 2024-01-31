package dev.koga.deeplinklauncher.android

import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.koga.deeplinklauncher.initKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        initKoin(
            appModule = module {
                single<Context> { this@App }
            },
        )
    }
}
