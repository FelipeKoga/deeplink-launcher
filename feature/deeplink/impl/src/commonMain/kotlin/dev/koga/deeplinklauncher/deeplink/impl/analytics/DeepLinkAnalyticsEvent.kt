package dev.koga.deeplinklauncher.deeplink.impl.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.model.LaunchSource

internal sealed interface DeepLinkAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data class DeeplinkLaunched(
    val source: LaunchSource,
) : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_launched"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal data class DeeplinkLaunchFailed(
    val source: LaunchSource,
) : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_launch_failed"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal data class DeeplinkCreated(
    val source: LaunchSource,
) : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_created"

    override fun parameters(): Map<String, String> = mapOf(
        "source" to source.value,
    )
}

internal data object DeeplinkShared : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_shared"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data class DeeplinkPinned(
    val result: String,
) : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_pinned"

    override fun parameters(): Map<String, String> = mapOf(
        "result" to result,
    )
}

internal data object DeeplinkLinkCopied : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_link_copied"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data object DeeplinkDeleted : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_deleted"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data class DeeplinkDuplicated(
    val copyAllFields: Boolean,
) : DeepLinkAnalyticsEvent {
    override val name: String = "deeplink_duplicated"

    override fun parameters(): Map<String, String> = mapOf(
        "copy_all_fields" to copyAllFields.toString(),
    )
}

internal data class FavoriteToggled(
    val isFavorite: Boolean,
) : DeepLinkAnalyticsEvent {
    override val name: String = "favorite_toggled"

    override fun parameters(): Map<String, String> = mapOf(
        "is_favorite" to isFavorite.toString(),
    )
}

internal data object FolderCreated : DeepLinkAnalyticsEvent {
    override val name: String = "folder_created"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data object FolderLinkCompleted : DeepLinkAnalyticsEvent {
    override val name: String = "folder_link_completed"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal data object FolderDeleted : DeepLinkAnalyticsEvent {
    override val name: String = "folder_deleted"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal fun AnalyticsTracker.track(event: DeepLinkAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
