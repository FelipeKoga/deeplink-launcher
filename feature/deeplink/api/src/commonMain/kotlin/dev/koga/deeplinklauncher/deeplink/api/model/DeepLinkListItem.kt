package dev.koga.deeplinklauncher.deeplink.api.model

public data class DeepLinkListItem(
    val deepLink: DeepLink,
    val iconPng: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DeepLinkListItem

        return deepLink.id == other.deepLink.id
    }

    override fun hashCode(): Int = deepLink.id.hashCode()
}
