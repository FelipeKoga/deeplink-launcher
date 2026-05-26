package dev.koga.deeplinklauncher.home.impl.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

internal sealed interface LaunchTargetAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data class LaunchTargetSelected(
    val targetType: String,
) : LaunchTargetAnalyticsEvent {
    override val name: String = "launch_target_selected"

    override fun parameters(): Map<String, String> = mapOf(
        "target_type" to targetType,
    )
}

internal fun AnalyticsTracker.track(event: LaunchTargetAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
