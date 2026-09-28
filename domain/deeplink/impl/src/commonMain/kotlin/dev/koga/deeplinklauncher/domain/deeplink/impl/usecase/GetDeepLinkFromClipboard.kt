package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.ClipboardTextReader
internal class GetDeepLinkFromClipboard(
    private val clipboard: ClipboardTextReader,
    private val validateDeepLink: ValidateDeepLink,
) {
    operator fun invoke(): String? = clipboard.read()?.trim()
        ?.takeIf { DEEPLINK_PATTERN.matches(it) && validateDeepLink.isValid(it) }

    private companion object {
        val DEEPLINK_PATTERN = Regex("^[A-Za-z][A-Za-z0-9+.-]+:\\S+$")
    }
}
