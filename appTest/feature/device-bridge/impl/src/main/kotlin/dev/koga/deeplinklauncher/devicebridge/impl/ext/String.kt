package dev.koga.deeplinklauncher.devicebridge.impl.ext

import dev.koga.deeplinklauncher.devicebridge.api.model.Os

internal fun String.installed(): Boolean {
    val process = ProcessBuilder().command(
        when (Os.get()) {
            Os.LINUX -> "which"
            Os.WINDOWS -> "where"
            Os.MAC -> "which"
        },
        this,
    ).start()

    return process.waitFor() == 0
}

internal fun String.get(regex: Regex): MatchResult {
    val match = regex.find(this) ?: error("No match found")

    return match
}
