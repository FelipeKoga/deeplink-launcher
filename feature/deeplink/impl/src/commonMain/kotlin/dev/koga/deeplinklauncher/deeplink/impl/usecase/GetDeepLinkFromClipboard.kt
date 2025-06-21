package dev.koga.deeplinklauncher.deeplink.impl.usecase

internal expect class GetDeepLinkFromClipboard {
    operator fun invoke(): String?
}
