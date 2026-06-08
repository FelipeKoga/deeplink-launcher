package dev.koga.deeplinklauncher.analytics.impl

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(name: String, parameters: Map<String, String>) = Unit
}
