package dev.koga.deeplinklauncher.model

class Adb(private val path: String) {

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
