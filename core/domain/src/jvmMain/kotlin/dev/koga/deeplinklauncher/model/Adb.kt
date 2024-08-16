package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.util.ext.installed

class Adb(private val path: String) {

    val installed get() = path.installed()

    fun startActivity(
        target: Target.Device,
        action: String,
        arg: String,
    ): Process {
        val process = ProcessBuilder().command(
            path,
            "-s", target.serial,
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

    fun getProperty(target: Target.Device, key: String): String {
        val process = ProcessBuilder().command(
            path,
            "-s", target.serial,
            "shell",
            "getprop", key
        ).start()

        process.waitFor()

        return process.inputStream.bufferedReader().readText().trim()
    }

    fun getDeviceName(target: Target.Device): String {

        val process = ProcessBuilder().command(
            path,
            "-s", target.serial,
            "shell",
            "settings",
            "get",
            "global",
            "device_name"
        ).start()

        process.waitFor()

        return process.inputStream.bufferedReader().readText().trim()
    }

    fun getDeviceModel(target: Target.Device): String {
        return getProperty(target, key = "ro.product.model")
    }

    fun getEmulatorName(target: Target.Device): String {
        return getProperty(target, key = "ro.kernel.qemu.avd_name")
    }

    companion object {
        fun from(directory: String): Adb {
            return Adb("$directory/platform-tools/adb")
        }
    }
}
