package dev.koga.deeplinklauncher.domain.deeplink.api.model
public data class DeepLinkMetadata(
    val scheme: String?,
    val host: String?,
    val path: String?,
    val query: String?,
)
