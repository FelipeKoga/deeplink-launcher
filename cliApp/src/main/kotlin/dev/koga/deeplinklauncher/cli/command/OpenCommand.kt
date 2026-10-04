package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import com.github.ajalt.clikt.parameters.types.restrictTo
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.LinkActions
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.output.OpenReport
import dev.koga.deeplinklauncher.cli.output.OpenStatus

internal class OpenCommand(private val toolchain: () -> Toolchain) : ReportingCommand<OpenReport>(
    name = "open",
    summary = "Open a link on a device and report what happened.",
    details = "Opens the link the way a tap in a browser would, then waits --watch milliseconds and checks that " +
        "the app that received it is still running. Reports the handler, the launch time on Android, " +
        "and the crash log when the app died. Exits with 1 when no app handled the link or the app crashed.",
    serializer = OpenReport.serializer(),
) {
    private val url by argument("URL", help = "The deeplink to open")
    private val target by TargetOptions()
    private val app by option(
        "--app",
        metavar = "BUNDLE_ID",
        help = "App to open the link in. Required on physical iPhones, where devicectl opens links inside a given app",
    )
    private val watchMs by option(
        "--watch",
        metavar = "MS",
        help = "How long to watch the app after opening (default 1500)",
    )
        .long()
        .restrictTo(min = 0)
        .default(DEFAULT_WATCH_MS)

    override fun execute(): Outcome<OpenReport> {
        val toolchain = toolchain()
        val link = requireLink(url)
        val report = LinkActions(toolchain).open(target.select(toolchain), link, watchMs, app)
        return Outcome(report, if (report.status == OpenStatus.OPENED) ExitCode.OK else ExitCode.FAILED)
    }

    override fun render(report: OpenReport): String {
        val where = "${report.device.name} (${report.device.summary})"
        val title = when (report.status) {
            OpenStatus.OPENED -> "${Text.OK} Opened on $where"
            OpenStatus.UNHANDLED -> "${Text.FAIL} Not handled on $where"
            OpenStatus.CRASHED -> "${Text.FAIL} Crashed on $where"
        }
        return title + "\n" + Text.fields(
            "url" to report.url,
            "handler" to report.handler?.label(),
            "launch" to listOfNotNull(report.launch, report.timeMs?.let { "$it ms" })
                .joinToString(" · ")
                .ifEmpty { null },
            "alive" to report.alive?.let { alive -> "${if (alive) "yes" else "no"}, after ${report.watchMs} ms" },
            "note" to report.note,
            "crash" to report.crash,
        )
    }

    private companion object {
        const val DEFAULT_WATCH_MS = 1500L
    }
}
