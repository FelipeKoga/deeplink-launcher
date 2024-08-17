package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdbProgram(private val path: String) {

    val installed get() = path.installed()

    suspend fun startActivity(
        target: Target.Device,
        action: String,
        arg: String,
    ): Process {

        return withContext(Dispatchers.IO) {
            ProcessBuilder().command(
                path,
                "-s", target.serial,
                "shell",
                "am", "start",
                "-a", action,
                "-d", arg,
            ).start().also {
                it.waitFor()
            }
        }
    }

    suspend fun trackDevices(): Process {

        return withContext(Dispatchers.IO) {
            ProcessBuilder().command(
                path,
                "track-devices",
                "--proto-text"
            ).start()
        }
    }

    suspend fun getProperty(serial: String, key: String): String {

        val process = withContext(Dispatchers.IO) {
            ProcessBuilder().command(
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

    suspend fun getDeviceName(serial: String): String {

        val process = withContext(Dispatchers.IO) {
            ProcessBuilder().command(
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

    suspend fun getDeviceModel(serial: String): String {

        return getProperty(
            serial = serial,
            key = "ro.product.model"
        )
    }

    suspend fun getEmulatorName(serial: String): String {

        return getProperty(
            serial = serial,
            key = "ro.kernel.qemu.avd_name"
        )
    }

    companion object {
        fun from(directory: String): AdbProgram {
            return AdbProgram("$directory/platform-tools/adb")
        }
    }
}
