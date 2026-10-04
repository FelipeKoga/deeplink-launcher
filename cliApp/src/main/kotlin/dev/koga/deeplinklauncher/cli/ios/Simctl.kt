package dev.koga.deeplinklauncher.cli.ios

import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.process.CommandResult
import dev.koga.deeplinklauncher.cli.process.CommandRunner

internal class Simctl(
    val xcrunPath: String,
    private val runner: CommandRunner,
) {

    fun isAvailable(): Boolean = command("help").succeeded

    fun bootedSimulators(): List<Device> =
        allSimulators().filter(Simulator::booted).map { simulator ->
            Device(
                id = simulator.udid,
                name = simulator.name,
                platform = Platform.IOS,
                virtual = true,
                osVersion = simulator.osVersion,
            )
        }

    fun allSimulators(): List<Simulator> {
        val result = command("list", "-j", "devices", "available")
        return if (result.succeeded) SimctlOutput.simulators(result.stdout) else emptyList()
    }

    fun handlers(udid: String, scheme: String): List<IosApp> =
        apps(udid).filter { app -> urlSchemes(app).any { it.equals(scheme, ignoreCase = true) } }

    fun open(udid: String, url: String): CommandResult = command("openurl", udid, url)

    fun runningBundleIds(udid: String): Set<String> =
        SimctlOutput.runningBundleIds(command("spawn", udid, "launchctl", "list").stdout)

    private fun apps(udid: String): List<IosApp> {
        val plist = command("listapps", udid).stdout
        val json = runner.run(listOf(PLUTIL, "-convert", "json", "-o", "-", "-"), input = plist)
        return if (json.succeeded) SimctlOutput.apps(json.stdout) else emptyList()
    }

    private fun urlSchemes(app: IosApp): List<String> {
        val infoPlist = "${app.path}/Info.plist"
        val result = runner.run(listOf(PLUTIL, "-extract", "CFBundleURLTypes", "json", "-o", "-", infoPlist))
        return if (result.succeeded) SimctlOutput.urlSchemes(result.stdout) else emptyList()
    }

    private fun command(vararg args: String) = runner.run(listOf(xcrunPath, "simctl", *args))

    companion object {
        const val UNHANDLED_URL_EXIT_CODE = 194
        private const val PLUTIL = "/usr/bin/plutil"
    }
}
