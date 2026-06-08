package dev.koga.deeplinklauncher.analytics.api

interface AnalyticsTracker {
    fun logEvent(name: String, parameters: Map<String, String> = emptyMap())
}
