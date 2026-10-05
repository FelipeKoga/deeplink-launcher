package dev.koga.deeplinklauncher.cli.ios

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.process.CommandRunner
import java.io.File

internal class DeviceCtl(
    private val xcrunPath: String,
    private val runner: CommandRunner,
) {

    fun isAvailable(): Boolean = runner.run(listOf(xcrunPath, "devicectl", "--version")).succeeded

    fun connectedDevices(): List<PhysicalDevice> =
        withJsonOutput("list", "devices", "--timeout", "5")?.let(DeviceCtlOutput::devices).orEmpty()

    fun devices(): List<Device> = connectedDevices().map { device ->
        Device(id = device.udid, name = device.name, platform = Platform.IOS, virtual = false, osVersion = device.osVersion)
    }

    fun launch(udid: String, bundleId: String, url: String): DeviceCtlLaunch {
        val output = withJsonOutput(
            "device", "process", "launch",
            "--device", udid,
            "--terminate-existing",
            "--payload-url", url,
            bundleId,
        ) ?: return DeviceCtlLaunch.Failed("devicectl did not report a result.")
        return DeviceCtlOutput.launch(output)
    }

    fun runningProcessIds(udid: String): Set<Int> =
        withJsonOutput("device", "info", "processes", "--device", udid)?.let(DeviceCtlOutput::runningProcessIds).orEmpty()

    private fun withJsonOutput(vararg args: String): String? {
        val output = File.createTempFile("deeplink-devicectl", ".json")
        return try {
            runner.run(listOf(xcrunPath, "devicectl", *args, "--json-output", output.path))
            output.readText().ifBlank { null }
        } finally {
            output.delete()
        }
    }

    companion object {
        fun requireApp(app: String?): String = app ?: throw CliFailure(
            exitCode = ExitCode.USAGE,
            message = "Physical iPhones open a link inside a specific app; pass its bundle id with --app.",
            hint = "List installed apps with `xcrun devicectl device info apps --device <id>`. " +
                "In a test suite, set \"expect\": { \"ios\": \"<bundle id>\" } on each link.",
        )
    }
}
