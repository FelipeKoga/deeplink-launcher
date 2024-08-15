package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Adb
import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed

object AdbProvider {

    operator fun invoke(): Adb {
        if ("adb".installed()) {
            return Adb("adb")
        }

        System.getenv("ANDROID_HOME")?.let {
            return Adb.from(it)
        }

        val userHome = System.getProperty("user.home")

        return when (Os.get()) {
            Os.LINUX -> {
                Adb.from("$userHome/Android/Sdk")
            }

            Os.WINDOWS -> {
                Adb.from("$userHome/AppData/Local/Android/Sdk")
            }
        }
    }
}
