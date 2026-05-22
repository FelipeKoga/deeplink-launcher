package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

public interface GetDeepLinkHandlerIcon {
    public suspend operator fun invoke(link: String): ByteArray?
}
