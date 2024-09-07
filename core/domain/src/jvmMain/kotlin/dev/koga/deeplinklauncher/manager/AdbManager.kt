package dev.koga.deeplinklauncher.manager

import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AdbManager {

    val installed: Boolean

    suspend fun execute(
        serial: String,
        action: String,
        arg: String,
    ): Process

    suspend fun trackDevices(): Process

    suspend fun getProperty(serial: String, key: String): String

    suspend fun getDeviceName(serial: String): String

    suspend fun getEmulatorName(serial: String): String

    suspend fun getDeviceModel(serial: String): String
}

internal class AdbManagerImpl(
    private val path: String
) : AdbManager {

    override val installed get() = path.installed()

    override suspend fun execute(
        serial: String,
        action: String,
        arg: String,
    ): Process {
        return withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "-s", serial,
                "shell",
                "am", "start",
                "-a", action,
                "-d", arg,
            ).start().also {
                it.waitFor()
            }
        }
    }

    override suspend fun trackDevices(): Process {
        return withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "track-devices",
                "--proto-text"
            ).start()
        }
    }

    override suspend fun getProperty(serial: String, key: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "-s", serial,
                "shell",
                "getprop", key
            ).start().also {
                it.waitFor()
            }
        }

        return process
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
    }

    override suspend fun getDeviceName(serial: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "-s", serial,
                "shell",
                "settings",
                "get",
                "global",
                "device_name"
            ).start().also {
                it.waitFor()
            }
        }

        return process
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
    }

    override suspend fun getDeviceModel(serial: String): String {
        return getProperty(
            serial = serial,
            key = "ro.product.model"
        )
    }

    override suspend fun getEmulatorName(serial: String): String {
        return getProperty(
            serial = serial,
            key = "ro.kernel.qemu.avd_name"
        )
    }

    companion object {
        fun build(): AdbManager {
            if ("adb".installed()) {
                return AdbManagerImpl("adb")
            }

            System.getenv("ANDROID_HOME")?.let {
                return AdbManagerImpl(it)
            }

            val userHome = System.getProperty("user.home")

            return when (Os.get()) {
                Os.LINUX -> {
                    AdbManagerImpl("$userHome/Android/Sdk/platform-tools/adb")
                }

                Os.WINDOWS -> {
                    AdbManagerImpl("$userHome/AppData/Local/Android/Sdk/platform-tools/adb")
                }

                Os.MAC -> {
                    AdbManagerImpl("$userHome/Library/Android/sdk/platform-tools/adb")
                }
            }
        }
    }
}
