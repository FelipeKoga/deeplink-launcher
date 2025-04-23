package dev.koga.deeplinklauncher.deeplink.common.usecase

public interface GetDeepLinkFromClipboard {
    public operator fun invoke(): String?
}
