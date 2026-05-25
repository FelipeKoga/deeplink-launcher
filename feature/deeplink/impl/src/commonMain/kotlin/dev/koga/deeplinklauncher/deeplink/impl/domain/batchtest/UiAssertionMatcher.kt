package dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion

internal fun extractVisibleTextsFromUiDump(uiDump: String): List<String> {
    if (uiDump.isBlank()) return emptyList()

    val attributePattern = Regex("""(?:text|content-desc)="([^"]+)"""")
    return attributePattern
        .findAll(uiDump)
        .mapNotNull { match ->
            match.groupValues
                .getOrNull(1)
                ?.trim()
                ?.takeIf { it.isNotBlank() }
        }
        .distinct()
        .sorted()
        .toList()
}

internal fun matchAssertionTexts(
    uiDump: String,
    expectedTexts: List<String>,
): Pair<List<String>, List<String>> {
    val normalizedDump = uiDump.lowercase()
    val matched = mutableListOf<String>()
    val missing = mutableListOf<String>()

    expectedTexts
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .forEach { expected ->
            if (normalizedDump.contains(expected.lowercase())) {
                matched += expected
            } else {
                missing += expected
            }
        }

    return matched to missing
}

internal fun matchesActivityPattern(
    activityClass: String,
    pattern: String,
): Boolean {
    return runCatching {
        Regex(pattern).containsMatchIn(activityClass)
    }.getOrDefault(false)
}

internal fun DeepLinkAssertion.sanitize(): DeepLinkAssertion {
    return copy(
        expectedPackage = expectedPackage?.trim()?.takeIf { it.isNotBlank() },
        expectedActivityPattern = expectedActivityPattern?.trim()?.takeIf { it.isNotBlank() },
        expectedTexts = expectedTexts.map { it.trim() }.filter { it.isNotBlank() },
        waitAfterLaunchMs = waitAfterLaunchMs.coerceIn(
            DeepLinkAssertion.MIN_WAIT_AFTER_LAUNCH_MS,
            DeepLinkAssertion.MAX_WAIT_AFTER_LAUNCH_MS,
        ),
    )
}
