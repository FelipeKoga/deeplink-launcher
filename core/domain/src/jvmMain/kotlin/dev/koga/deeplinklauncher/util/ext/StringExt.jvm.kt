package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.Os

fun String.installed(): Boolean {
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

inline fun String.dropWhile(
    n: Int,
    predicate: (Char) -> Boolean
): String {

    for ((count, index) in indices.withIndex()) {
        if (count >= n || !predicate(this[index])) {
            return substring(index)
        }
    }

    return ""
}

fun String.get(regex: Regex): MatchResult {

    val match = regex.find(this) ?: error("No match found")

    return match
}
