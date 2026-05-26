package dev.koga.deeplinklauncher.settings.impl.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

internal sealed interface SettingsAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data class ThemeChanged(
    val theme: String,
) : SettingsAnalyticsEvent {
    override val name: String = "theme_changed"

    override fun parameters(): Map<String, String> = mapOf(
        "theme" to theme,
    )
}

internal data class SuggestionsToggled(
    val enabled: Boolean,
) : SettingsAnalyticsEvent {
    override val name: String = "suggestions_toggled"

    override fun parameters(): Map<String, String> = mapOf(
        "enabled" to enabled.toString(),
    )
}

internal data class DataDeleted(
    val deletionType: String,
) : SettingsAnalyticsEvent {
    override val name: String = "data_deleted"

    override fun parameters(): Map<String, String> = mapOf(
        "deletion_type" to deletionType,
    )
}

internal data class ExternalLinkOpened(
    val destination: String,
) : SettingsAnalyticsEvent {
    override val name: String = "external_link_opened"

    override fun parameters(): Map<String, String> = mapOf(
        "destination" to destination,
    )
}

internal data class PurchaseStarted(
    val productId: String,
) : SettingsAnalyticsEvent {
    override val name: String = "purchase_started"

    override fun parameters(): Map<String, String> = mapOf(
        "product_id" to productId,
    )
}

internal data class PurchaseCompleted(
    val productId: String,
) : SettingsAnalyticsEvent {
    override val name: String = "purchase_completed"

    override fun parameters(): Map<String, String> = mapOf(
        "product_id" to productId,
    )
}

internal data class PurchaseFailed(
    val productId: String,
    val userCancelled: Boolean,
) : SettingsAnalyticsEvent {
    override val name: String = "purchase_failed"

    override fun parameters(): Map<String, String> = mapOf(
        "product_id" to productId,
        "user_cancelled" to userCancelled.toString(),
    )
}

internal fun AnalyticsTracker.track(event: SettingsAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
