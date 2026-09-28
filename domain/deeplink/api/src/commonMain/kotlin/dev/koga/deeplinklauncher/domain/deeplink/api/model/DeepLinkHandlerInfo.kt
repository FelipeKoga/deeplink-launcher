package dev.koga.deeplinklauncher.domain.deeplink.api.model
public sealed class DeepLinkHandlerInfo {
    public data class Available(
        val canResolve: Boolean,
        val appName: String?,
        val packageName: String? = null,
    ) : DeepLinkHandlerInfo()

    public data object Unavailable : DeepLinkHandlerInfo()
}
