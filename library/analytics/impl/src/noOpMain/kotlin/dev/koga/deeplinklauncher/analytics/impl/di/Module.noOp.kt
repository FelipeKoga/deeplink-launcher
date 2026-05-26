package dev.koga.deeplinklauncher.analytics.impl.di

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.analytics.impl.NoOpAnalyticsTracker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val analyticsModule: Module = module {
    single<AnalyticsTracker> { NoOpAnalyticsTracker() }
}
