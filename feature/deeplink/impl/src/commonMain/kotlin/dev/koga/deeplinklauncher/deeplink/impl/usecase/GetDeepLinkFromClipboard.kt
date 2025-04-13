package dev.koga.deeplinklauncher.deeplink.impl.usecase

expect class GetDeepLinkFromClipboard {
    operator fun invoke(): String?
}
