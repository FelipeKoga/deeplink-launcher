package dev.koga.deeplinklauncher.cli.command

import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.output.DevicesReport

internal class DevicesCommand(private val toolchain: () -> Toolchain) : ReportingCommand<DevicesReport>(
    name = "devices",
    summary = "List running Android devices and emulators, booted iOS simulators and connected iPhones.",
    details = "Use the id with --device in other commands.",
    serializer = DevicesReport.serializer(),
) {
    override fun execute() = Outcome(DevicesReport(devices = toolchain().runningDevices()))

    override fun render(report: DevicesReport): String {
        if (report.devices.isEmpty()) {
            return "No running devices.\n  Start an Android emulator or boot an iOS simulator."
        }
        val rows = report.devices.map { device ->
            listOf(
                device.id,
                device.name,
                listOfNotNull(device.platform.label, device.osVersion).joinToString(" "),
                if (device.virtual) "virtual" else "physical",
            )
        }
        val widths = rows.first().indices.map { column -> rows.maxOf { it[column].length } }
        return "${Text.count(report.devices.size, "running device")}\n" + rows.joinToString("\n") { row ->
            "  " + row.mapIndexed { column, cell -> cell.padEnd(widths[column]) }.joinToString("   ").trimEnd()
        }
    }
}
