package dev.koga.deeplinklauncher.deeplink.api.ui.formatting

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public fun DeepLink.truncatedLink(): String = link.truncateDeepLinkMiddle()

private const val DEFAULT_DEEP_LINK_TRUNCATE_MAX_LENGTH = 60
private const val DEFAULT_DEEP_LINK_TRUNCATE_MIDDLE = "/.../"

public fun String.truncateDeepLinkMiddle(
    maxLength: Int = DEFAULT_DEEP_LINK_TRUNCATE_MAX_LENGTH,
    middle: String = DEFAULT_DEEP_LINK_TRUNCATE_MIDDLE,
): String {
    if (maxLength !in 1..<length) return this

    val parts = parseDeepLinkOriginAndRemainder() ?: return this
    val (origin, remainder) = parts

    if (remainder.isEmpty() || origin.length + remainder.length <= maxLength) return this

    val availableForSuffix = maxLength - origin.length - middle.length
    if (availableForSuffix <= 0) return origin

    val suffix = remainder.takeLast(availableForSuffix)
    return origin + middle + suffix
}

private fun String.parseDeepLinkOriginAndRemainder(): Pair<String, String>? {
    val schemeSeparatorIndex = indexOf(':')
    if (schemeSeparatorIndex <= 0) return null

    val scheme = substring(0, schemeSeparatorIndex)
    val afterScheme = substring(schemeSeparatorIndex + 1)

    if (!afterScheme.startsWith("//")) return null

    val afterAuthorityPrefix = afterScheme.substring(2)
    val remainderStartIndex = afterAuthorityPrefix.indexOfFirst { it == '/' || it == '?' || it == '#' }

    return if (remainderStartIndex == -1) {
        "$scheme://$afterAuthorityPrefix" to ""
    } else {
        val authority = afterAuthorityPrefix.substring(0, remainderStartIndex)
        val remainder = afterAuthorityPrefix.substring(remainderStartIndex)
        "$scheme://$authority" to remainder
    }
}
