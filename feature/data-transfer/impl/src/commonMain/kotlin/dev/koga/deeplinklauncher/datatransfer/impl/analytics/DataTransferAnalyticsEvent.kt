package dev.koga.deeplinklauncher.datatransfer.impl.analytics

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

internal sealed interface DataTransferAnalyticsEvent {
    val name: String

    fun parameters(): Map<String, String>
}

internal data class DataImported(
    val deeplinkCount: Int,
    val folderCount: Int,
) : DataTransferAnalyticsEvent {
    override val name: String = "data_imported"

    override fun parameters(): Map<String, String> = mapOf(
        "deeplink_count" to deeplinkCount.toString(),
        "folder_count" to folderCount.toString(),
    )
}

internal data object DataExported : DataTransferAnalyticsEvent {
    override val name: String = "data_exported"

    override fun parameters(): Map<String, String> = emptyMap()
}

internal fun AnalyticsTracker.track(event: DataTransferAnalyticsEvent) {
    logEvent(event.name, event.parameters())
}
