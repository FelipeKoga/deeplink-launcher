package dev.koga.deeplinklauncher.analytics.impl

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun init() = Unit

    override fun setUserProperty(name: String, value: String?) = Unit

    override fun logEvent(name: String, parameters: Map<String, String>) = Unit
}
