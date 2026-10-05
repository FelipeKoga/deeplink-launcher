package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.parameters.arguments.argument
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.link.ParsedLink
import dev.koga.deeplinklauncher.cli.output.ParseReport

internal class ParseCommand : ReportingCommand<ParseReport>(
    name = "parse",
    summary = "Split a link into scheme, host, path, query and fragment.",
    details = "Works offline, without a device. Exits with 1 when the text is not a link.",
    serializer = ParseReport.serializer(),
) {
    private val url by argument("URL", help = "The link to parse")

    override fun execute(): Outcome<ParseReport> {
        val link = ParsedLink(url)
        val report = ParseReport(
            text = link.text,
            valid = link.isValid,
            looksLikeDeepLink = link.looksLikeDeepLink,
            scheme = link.scheme,
            host = link.host,
            path = link.path,
            query = link.query,
            fragment = link.fragment,
        )
        return Outcome(report, if (link.isValid) ExitCode.OK else ExitCode.FAILED)
    }

    override fun render(report: ParseReport): String {
        if (!report.valid) return "${Text.FAIL} Not a link: ${report.text}"
        return "${Text.OK} ${report.text}\n" + Text.fields(
            "scheme" to report.scheme,
            "host" to report.host,
            "path" to report.path.ifEmpty { null },
            "query" to report.query,
            "fragment" to report.fragment,
        )
    }
}
