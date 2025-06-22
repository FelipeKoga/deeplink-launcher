package dev.koga.deeplinklauncher.deeplink.api.model

public data class DeepLinkMetadata(
    val link: String,
    val scheme: String?,
    val host: String?,
    val query: String?,
)
