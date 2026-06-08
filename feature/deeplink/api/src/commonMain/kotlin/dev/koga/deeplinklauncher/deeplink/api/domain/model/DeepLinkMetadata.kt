package dev.koga.deeplinklauncher.deeplink.api.domain.model

public data class DeepLinkMetadata(
    val scheme: String?,
    val host: String?,
    val path: String?,
    val query: String?,
)
