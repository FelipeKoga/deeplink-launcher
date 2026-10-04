package dev.koga.deeplinklauncher.cli.android

import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.process.CommandRunner

internal class Adb(
    val path: String,
    private val runner: CommandRunner,
) {

    fun version(): String? =
        runner.run(listOf(path, "version")).takeIf { it.succeeded }?.stdout?.lineSequence()?.firstOrNull()

    fun devices(): List<Device> =
        AdbOutput.devices(runner.run(listOf(path, "devices", "-l")).stdout)
            .filter { it.state == "device" }
            .map { line ->
                val emulator = line.serial.startsWith("emulator-")
                Device(
                    id = line.serial,
                    name = (if (emulator) avdName(line.serial) else null) ?: line.model ?: line.serial,
                    platform = Platform.ANDROID,
                    virtual = emulator,
                    osVersion = shell(line.serial, "getprop ro.build.version.release").stdout.trim().ifEmpty { null },
                )
            }

    fun handlers(serial: String, url: String): List<Component> =
        AdbOutput.components(shell(serial, "cmd package query-activities --brief ${viewIntent(url)}").stdout)

    fun defaultHandler(serial: String, url: String): Component? =
        AdbOutput.components(shell(serial, "cmd package resolve-activity --brief ${viewIntent(url)}").stdout)
            .lastOrNull()

    fun start(serial: String, url: String): AmStart {
        val result = shell(serial, "am start -W ${viewIntent(url)}")
        return AdbOutput.amStart(result.stdout + "\n" + result.stderr)
    }

    fun isRunning(serial: String, packageName: String): Boolean =
        shell(serial, "pidof ${quote(packageName)}").stdout.isNotBlank()

    fun deviceTime(serial: String): String =
        shell(serial, "date +'%m-%d %H:%M:%S.000'").stdout.trim()

    fun crashLog(serial: String, packageName: String, since: String): String? {
        val log = shell(serial, "logcat -b crash -d -t ${quote(since)}").stdout
        val process = log.lastIndexOf("Process: $packageName,")
        if (process < 0) return null
        val fatal = log.lastIndexOf("FATAL EXCEPTION", process).takeIf { it >= 0 } ?: process
        val blockStart = log.lastIndexOf('\n', fatal) + 1
        return log.substring(blockStart).lineSequence().take(MAX_CRASH_LINES).joinToString("\n").trim()
    }

    private fun shell(serial: String, command: String) =
        runner.run(listOf(path, "-s", serial, "shell", command))

    private fun avdName(serial: String): String? =
        runner.run(listOf(path, "-s", serial, "emu", "avd", "name")).stdout
            .lineSequence()
            .firstOrNull()
            ?.trim()
            ?.ifEmpty { null }

    private fun viewIntent(url: String) =
        "-a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d ${quote(url)}"

    companion object {
        private const val MAX_CRASH_LINES = 20

        fun quote(value: String): String = "'" + value.replace("'", "'\\''") + "'"
    }
}
