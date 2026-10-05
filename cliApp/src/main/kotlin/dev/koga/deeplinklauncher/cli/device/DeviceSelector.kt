package dev.koga.deeplinklauncher.cli.device

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode

internal object DeviceSelector {

    fun select(devices: List<Device>, id: String?, platform: Platform?): Device {
        if (id != null) {
            return devices.firstOrNull { it.id == id } ?: throw CliFailure(
                exitCode = ExitCode.DEVICE,
                message = "No running device with id $id.",
                hint = devices.hint(),
            )
        }

        val candidates = devices.filter { platform == null || it.platform == platform }
        return when (candidates.size) {
            1 -> candidates.single()
            0 -> throw CliFailure(
                exitCode = ExitCode.DEVICE,
                message = "No running ${platform?.label ?: "Android or iOS"} device.",
                hint = "Start an emulator or boot a simulator, then run `deeplink devices`.",
            )
            else -> throw CliFailure(
                exitCode = ExitCode.DEVICE,
                message = "${candidates.size} devices are running; choose one with --device.",
                hint = candidates.hint(),
            )
        }
    }

    private fun List<Device>.hint(): String =
        if (isEmpty()) {
            "Run `deeplink devices` to list running devices."
        } else {
            maxOf { it.id.length }.let { width ->
                joinToString(separator = "\n", prefix = "Running devices:\n") {
                    "  --device ${it.id.padEnd(width)}   ${it.name} (${it.platform.label})"
                }
            }
        }
}
