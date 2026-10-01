package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.platform.ClipboardTextReader

internal class GetDeepLinkFromClipboard(
    private val clipboard: ClipboardTextReader,
    private val validateDeepLink: ValidateDeepLink,
) {
    operator fun invoke(): String? = clipboard.read()?.trim()
        ?.takeIf { DEEPLINK_PATTERN.matches(it) && validateDeepLink.isValid(it) }

    fun canOfferPaste(): Boolean = clipboard.hasTextToPaste()

    private companion object {
        val DEEPLINK_PATTERN = Regex("^[A-Za-z][A-Za-z0-9+.-]+:\\S+$")
    }
}
