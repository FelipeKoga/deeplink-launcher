package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed

object ExecuteAdb {

    operator fun invoke(action: String, arg: String): Process {
        val process = ProcessBuilder().command(
            getAdbProgram(), "shell",
            "am", "start",
            "-a", action,
            "-d", arg
        )

        return process.start().also {
            it.waitFor()
        }
    }

    private fun getAdbProgram(): String {
        if ("adb".installed()) {
            return "adb"
        }

        System.getenv("ANDROID_HOME")?.let {
            return "$it/platform-tools/adb"
        }

        val userHome = System.getProperty("user.home")

        return when (Os.get()) {
            Os.LINUX -> {
                "$userHome/Android/Sdk/platform-tools/adb"
            }

            Os.WINDOWS -> {
                "$userHome/AppData/Local/Android/Sdk/platform-tools/adb.exe"
            }

            Os.MAC -> {
                "$userHome/Library/Android/sdk/platform-tools/adb"
            }
        }
    }
}
