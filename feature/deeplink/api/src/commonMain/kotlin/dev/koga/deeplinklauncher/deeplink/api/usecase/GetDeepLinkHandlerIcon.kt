package dev.koga.deeplinklauncher.deeplink.api.usecase

public interface GetDeepLinkHandlerIcon {
    public suspend operator fun invoke(link: String): ByteArray?
}
