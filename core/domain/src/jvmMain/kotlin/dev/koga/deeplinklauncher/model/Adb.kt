package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.util.ext.installed

class Adb(private val path: String) {

    val installed get() = path.installed()

    fun startActivity(
        target: Target,
        action: String,
        arg: String,
    ): Process {
        val process = ProcessBuilder().command(
            path,
            "-s", target.name,
            "shell",
            "am", "start",
            "-a", action,
            "-d", arg,
        )

        return process.start().also {
            it.waitFor()
        }
    }

    fun trackDevices(): Process {
        val process = ProcessBuilder().command(
            path, "track-devices"
        )

        return process.start()
    }

    companion object {
        fun from(directory: String): Adb {
            return Adb("$directory/platform-tools/adb")
        }
    }
}
