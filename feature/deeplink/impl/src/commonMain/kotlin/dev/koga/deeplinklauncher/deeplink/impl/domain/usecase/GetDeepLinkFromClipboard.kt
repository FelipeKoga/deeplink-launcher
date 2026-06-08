package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

internal expect class GetDeepLinkFromClipboard {
    operator fun invoke(): String?
}
