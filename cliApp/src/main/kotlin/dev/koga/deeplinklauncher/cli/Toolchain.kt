package dev.koga.deeplinklauncher.cli

import dev.koga.deeplinklauncher.cli.android.Adb
import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.ios.DeviceCtl
import dev.koga.deeplinklauncher.cli.ios.Simctl
import dev.koga.deeplinklauncher.cli.process.ProcessCommandRunner
import dev.koga.deeplinklauncher.cli.process.Tools

internal class Toolchain(
    val adb: Adb?,
    val simctl: Simctl?,
    val deviceCtl: DeviceCtl? = null,
    val sleep: (Long) -> Unit = Thread::sleep,
) {

    fun runningDevices(): List<Device> {
        if (adb == null && simctl == null && deviceCtl == null) {
            throw CliFailure(
                exitCode = ExitCode.TOOLING,
                message = "Neither adb nor xcrun was found.",
                hint = "Run `deeplink doctor` to see what is missing and how to install it.",
            )
        }
        return adb?.devices().orEmpty() + simctl?.bootedSimulators().orEmpty() + deviceCtl?.devices().orEmpty()
    }

    fun requireAdb(): Adb = adb ?: throw CliFailure(
        exitCode = ExitCode.TOOLING,
        message = "adb was not found.",
        hint = "Install the Android SDK platform-tools or set ANDROID_HOME.",
    )

    fun requireDeviceCtl(): DeviceCtl = deviceCtl ?: throw CliFailure(
        exitCode = ExitCode.TOOLING,
        message = "xcrun devicectl was not found.",
        hint = "Physical iPhones need Xcode 15 or newer.",
    )

    fun requireSimctl(): Simctl = simctl ?: throw CliFailure(
        exitCode = ExitCode.TOOLING,
        message = "xcrun was not found.",
        hint = "Install Xcode and run `sudo xcode-select --switch /Applications/Xcode.app`.",
    )

    companion object {
        fun system(): Toolchain {
            val runner = ProcessCommandRunner()
            val xcrun = Tools.findXcrun()
            return Toolchain(
                adb = Tools.findAdb()?.let { Adb(it, runner) },
                simctl = xcrun?.let { Simctl(it, runner) },
                deviceCtl = xcrun?.let { DeviceCtl(it, runner) },
            )
        }
    }
}
