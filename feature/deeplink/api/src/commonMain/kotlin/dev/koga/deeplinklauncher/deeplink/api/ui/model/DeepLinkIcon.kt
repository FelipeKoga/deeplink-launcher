package dev.koga.deeplinklauncher.deeplink.api.ui.model

public class DeepLinkIcon(public val id: Long, public val byteArray: ByteArray) {
    override fun equals(other: Any?): Boolean = other is DeepLinkIcon && other.id == id
    override fun hashCode(): Int = id.hashCode()
}
