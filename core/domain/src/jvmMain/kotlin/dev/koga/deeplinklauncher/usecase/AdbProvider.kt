package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.platform.AdbProgram
import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed

object AdbProvider {

    operator fun invoke(): AdbProgram {
        if ("adb".installed()) {
            return AdbProgram("adb")
        }

        System.getenv("ANDROID_HOME")?.let {
            return AdbProgram.from(it)
        }

        val userHome = System.getProperty("user.home")

        return when (Os.get()) {
            Os.LINUX -> {
                AdbProgram.from("$userHome/Android/Sdk")
            }

            Os.WINDOWS -> {
                AdbProgram.from("$userHome/AppData/Local/Android/Sdk")
            }

            Os.MAC -> {
                AdbProgram.from("$userHome/Library/Android/sdk")
            }
        }
    }
}
