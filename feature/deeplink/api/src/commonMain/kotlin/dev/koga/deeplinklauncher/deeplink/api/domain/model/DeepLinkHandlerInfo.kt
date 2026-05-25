package dev.koga.deeplinklauncher.deeplink.api.domain.model

public sealed class DeepLinkHandlerInfo {
    public data class Available(
        val canResolve: Boolean,
        val appName: String?,
    ) : DeepLinkHandlerInfo()

    public data object Unavailable : DeepLinkHandlerInfo()
}
