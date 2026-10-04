package dev.koga.deeplinklauncher.cli.command

import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.output.DoctorReport
import dev.koga.deeplinklauncher.cli.output.DoctorReport.Check
import dev.koga.deeplinklauncher.cli.process.Tools

internal class DoctorCommand(private val toolchain: () -> Toolchain) : ReportingCommand<DoctorReport>(
    name = "doctor",
    summary = "Check that adb, xcrun and a running device are available.",
    details = "Prints the exact command to fix each failed check. Exits with 4 when no tool is installed " +
        "and with 3 when the tools work but no device is running.",
    serializer = DoctorReport.serializer(),
) {
    override fun execute(): Outcome<DoctorReport> {
        val toolchain = toolchain()
        val checks = androidChecks(toolchain) + iosChecks(toolchain)
        val toolsFound = toolchain.adb != null || toolchain.simctl != null
        val deviceFound = checks.any { it.name in DEVICE_CHECKS && it.ok }
        val exitCode = when {
            !toolsFound -> ExitCode.TOOLING
            !deviceFound -> ExitCode.DEVICE
            else -> ExitCode.OK
        }
        return Outcome(DoctorReport(ready = exitCode == ExitCode.OK, checks = checks), exitCode)
    }

    override fun render(report: DoctorReport): String {
        val lines = report.checks.joinToString("\n") { check ->
            val mark = if (check.ok) Text.OK else Text.FAIL
            listOfNotNull("$mark ${check.name}: ${check.detail}", check.fix?.let { "    fix: $it" }).joinToString("\n")
        }
        val verdict = if (report.ready) "Ready to open links." else "Not ready: fix the items marked ${Text.FAIL}."
        return "$lines\n\n$verdict"
    }

    private fun androidChecks(toolchain: Toolchain): List<Check> {
        val adb = toolchain.adb ?: return listOf(
            Check(
                name = "adb",
                ok = false,
                detail = "not found on PATH, ANDROID_HOME or the default SDK location",
                fix = "Install the Android SDK platform-tools and set ANDROID_HOME, " +
                    "e.g. `brew install --cask android-platform-tools`",
            ),
        )
        val devices = adb.devices()
        return listOf(
            Check(name = "adb", ok = true, detail = listOfNotNull(adb.path, adb.version()).joinToString(" · ")),
            Check(
                name = "Android devices",
                ok = devices.isNotEmpty(),
                detail = devices.joinToString { "${it.name} (${it.id})" }.ifEmpty { "none running" },
                fix = START_EMULATOR.takeIf { devices.isEmpty() },
            ),
        )
    }

    private fun iosChecks(toolchain: Toolchain): List<Check> {
        if (!Tools.isMac) {
            return listOf(Check(name = "iOS simulators", ok = false, detail = "iOS simulators need macOS"))
        }
        val simctl = toolchain.simctl?.takeIf { it.isAvailable() } ?: return listOf(
            Check(
                name = "xcrun simctl",
                ok = false,
                detail = "not available",
                fix = "Install Xcode, then run `sudo xcode-select --switch /Applications/Xcode.app`",
            ),
        )
        val booted = simctl.bootedSimulators()
        return listOf(
            Check(name = "xcrun simctl", ok = true, detail = simctl.xcrunPath),
            Check(
                name = "iOS simulators",
                ok = booted.isNotEmpty(),
                detail = booted.joinToString { "${it.name} (${it.id})" }.ifEmpty { "no simulator booted" },
                fix = BOOT_SIMULATOR.takeIf { booted.isEmpty() },
            ),
        ) + physicalIosChecks(toolchain)
    }

    private fun physicalIosChecks(toolchain: Toolchain): List<Check> {
        val deviceCtl = toolchain.deviceCtl?.takeIf { it.isAvailable() } ?: return listOf(
            Check(name = "iPhones", ok = false, detail = "xcrun devicectl not available", fix = "Install Xcode 15 or newer"),
        )
        val devices = deviceCtl.connectedDevices()
        val developerModeOff = devices.filter { it.developerMode != null && it.developerMode != "enabled" }
        return listOf(
            Check(
                name = "iPhones",
                ok = devices.isNotEmpty() && developerModeOff.isEmpty(),
                detail = devices.joinToString { "${it.name} (${it.udid}, developer mode ${it.developerMode ?: "unknown"})" }
                    .ifEmpty { "none connected" },
                fix = when {
                    developerModeOff.isNotEmpty() -> "Turn on Settings › Privacy & Security › Developer Mode on the iPhone"
                    devices.isEmpty() -> CONNECT_IPHONE
                    else -> null
                },
            ),
        )
    }

    private companion object {
        val DEVICE_CHECKS = setOf("Android devices", "iOS simulators", "iPhones")
        const val CONNECT_IPHONE = "Connect it with a cable, tap Trust, and enable Developer Mode " +
            "(Settings › Privacy & Security). Optional: only needed to test on a physical iPhone"
        const val START_EMULATOR = "Start one with `emulator -list-avds` then `emulator -avd <name>`, " +
            "or plug in a device with USB debugging"
        const val BOOT_SIMULATOR = "Boot one with `xcrun simctl boot \"iPhone 16\"` or `open -a Simulator`"
    }
}
