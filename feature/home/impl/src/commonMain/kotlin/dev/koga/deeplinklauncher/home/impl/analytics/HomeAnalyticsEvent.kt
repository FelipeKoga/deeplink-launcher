package dev.koga.deeplinklauncher.home.impl.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.model.LaunchSource

internal sealed interface HomeAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data object OnboardingViewed : HomeAnalyticsEvent {
    override val name: String = "onboarding_viewed"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data object OnboardingCompleted : HomeAnalyticsEvent {
    override val name: String = "onboarding_completed"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data class DeeplinkLaunched(
    val source: LaunchSource,
) : HomeAnalyticsEvent {
    override val name: String = "deeplink_launched"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal data class DeeplinkLaunchFailed(
    val source: LaunchSource,
) : HomeAnalyticsEvent {
    override val name: String = "deeplink_launch_failed"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal data class DeeplinkCreated(
    val source: LaunchSource,
) : HomeAnalyticsEvent {
    override val name: String = "deeplink_created"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal enum class HomeTab(val value: String) {
    HISTORY("history"),
    FAVORITES("favorites"),
    FOLDERS("folders"),
}

internal data class HomeTabSelected(
    val tab: HomeTab,
) : HomeAnalyticsEvent {
    override val name: String = "home_tab_selected"

    override fun parameters(): Map<String, String> = mapOf(
        "tab" to tab.value,
    )
}

internal data object SearchUsed : HomeAnalyticsEvent {
    override val name: String = "search_used"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data class DeeplinkDetailsOpened(
    val entryPoint: String,
) : HomeAnalyticsEvent {
    override val name: String = "deeplink_details_opened"

    override fun parameters(): Map<String, String> = mapOf(
        "entry_point" to entryPoint,
    )
}

internal data class FavoriteToggled(
    val isFavorite: Boolean,
) : HomeAnalyticsEvent {
    override val name: String = "favorite_toggled"

    override fun parameters(): Map<String, String> = mapOf(
        "is_favorite" to isFavorite.toString(),
    )
}

internal fun AnalyticsTracker.track(event: HomeAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
