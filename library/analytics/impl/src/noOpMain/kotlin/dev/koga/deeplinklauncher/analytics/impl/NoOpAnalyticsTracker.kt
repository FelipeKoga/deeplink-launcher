package dev.koga.deeplinklauncher.analytics.impl

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
internal class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(name: String, parameters: Map<String, String>) = Unit
}
