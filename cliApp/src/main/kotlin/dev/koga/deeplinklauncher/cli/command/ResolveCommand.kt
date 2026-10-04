package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.LinkActions
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.output.ResolveReport
import dev.koga.deeplinklauncher.cli.output.ResolveStatus

internal class ResolveCommand(private val toolchain: () -> Toolchain) : ReportingCommand<ResolveReport>(
    name = "resolve",
    summary = "Show which apps on the device would handle a link, without opening it.",
    details = "Android lists every activity that accepts the link from a browser, and the default one. " +
        "iOS lists the installed apps that declare the link's custom scheme. " +
        "Exits with 1 when no app handles the link.",
    serializer = ResolveReport.serializer(),
) {
    private val url by argument("URL", help = "The deeplink to resolve")
    private val target by TargetOptions()

    override fun execute(): Outcome<ResolveReport> {
        val toolchain = toolchain()
        val report = LinkActions(toolchain).resolve(target.select(toolchain), url)
        return Outcome(report, if (report.status == ResolveStatus.UNHANDLED) ExitCode.FAILED else ExitCode.OK)
    }

    override fun render(report: ResolveReport): String {
        val title = when (report.status) {
            ResolveStatus.HANDLED -> "${Text.OK} ${Text.count(report.handlers.size, "app")} can open ${report.url}"
            ResolveStatus.UNHANDLED -> "${Text.FAIL} No app on ${report.device.name} handles ${report.url}"
            ResolveStatus.UNKNOWN -> "? Cannot tell which app opens ${report.url}"
        }
        return title + "\n" + Text.fields(
            "device" to "${report.device.name} (${report.device.summary})",
            "default" to report.defaultHandler?.label(),
            "handlers" to report.handlers.takeIf { it.size > 1 }?.joinToString("\n") { it.label() },
            "note" to report.note,
        )
    }
}
