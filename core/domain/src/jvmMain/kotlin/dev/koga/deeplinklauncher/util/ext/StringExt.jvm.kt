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