package dev.koga.deeplinklauncher.cli

import dev.koga.deeplinklauncher.cli.android.Adb
import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.ios.Simctl
import dev.koga.deeplinklauncher.cli.process.ProcessCommandRunner
import dev.koga.deeplinklauncher.cli.process.Tools

internal class Toolchain(
    val adb: Adb?,
    val simctl: Simctl?,
    val sleep: (Long) -> Unit = Thread::sleep,
) {

    fun runningDevices(): List<Device> {
        if (adb == null && simctl == null) {
            throw CliFailure(
                exitCode = ExitCode.TOOLING,
                message = "Neither adb nor xcrun was found.",
                hint = "Run `deeplink doctor` to see what is missing and how to install it.",
            )
        }
        return adb?.devices().orEmpty() + simctl?.bootedSimulators().orEmpty()
    }

    fun requireAdb(): Adb = adb ?: throw CliFailure(
        exitCode = ExitCode.TOOLING,
        message = "adb was not found.",
        hint = "Install the Android SDK platform-tools or set ANDROID_HOME.",
    )

    fun requireSimctl(): Simctl = simctl ?: throw CliFailure(
        exitCode = ExitCode.TOOLING,
        message = "xcrun was not found.",
        hint = "Install Xcode and run `sudo xcode-select --switch /Applications/Xcode.app`.",
    )

    companion object {
        fun system(): Toolchain {
            val runner = ProcessCommandRunner()
            return Toolchain(
                adb = Tools.findAdb()?.let { Adb(it, runner) },
                simctl = Tools.findXcrun()?.let { Simctl(it, runner) },
            )
        }
    }
}
