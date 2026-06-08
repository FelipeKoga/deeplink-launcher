package dev.koga.deeplinklauncher.shared.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

internal sealed interface AppAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data object AppOpen : AppAnalyticsEvent {
    override val name: String = "app_open"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data class ScreenViewed(
    val screenName: String,
) : AppAnalyticsEvent {
    override val name: String = "screen_view"

    override fun parameters(): Map<String, String> = mapOf(
        "screen_name" to screenName,
    )
}

internal fun AnalyticsTracker.track(event: AppAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
