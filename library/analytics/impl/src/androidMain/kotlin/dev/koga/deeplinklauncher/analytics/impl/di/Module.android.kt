package dev.koga.deeplinklauncher.analytics.impl.di

import android.content.Context
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.analytics.impl.FirebaseAnalyticsTracker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val analyticsModule: Module = module {
    single<AnalyticsTracker> { FirebaseAnalyticsTracker(get<Context>()) }
}
