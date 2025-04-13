package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import platform.UIKit.UIPasteboard

actual class GetDeepLinkFromClipboard(
    private val validateDeepLink: ValidateDeepLink,
) {
    actual operator fun invoke(): String? {
        val text = UIPasteboard.generalPasteboard.string ?: return null

        if (!validateDeepLink.isValid(text)) return null

        return text
    }
}