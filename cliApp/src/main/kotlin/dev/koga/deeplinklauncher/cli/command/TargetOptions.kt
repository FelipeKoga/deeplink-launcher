package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.DeviceSelector
import dev.koga.deeplinklauncher.cli.device.Platform

internal class TargetOptions : OptionGroup() {
    val device by option(
        "--device",
        "-d",
        help = "Device id from `deeplink devices`. Required when several devices are running",
    )
    val platform by option("--platform", "-p", help = "Only consider devices of this platform")
        .enum<Platform> { it.name.lowercase() }

    fun select(toolchain: Toolchain): Device = DeviceSelector.select(toolchain.runningDevices(), device, platform)
}
