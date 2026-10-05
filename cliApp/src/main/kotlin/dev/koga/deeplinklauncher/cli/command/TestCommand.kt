package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import com.github.ajalt.clikt.parameters.types.restrictTo
import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.LinkActions
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.output.TestReport
import dev.koga.deeplinklauncher.cli.output.TestStatus
import dev.koga.deeplinklauncher.cli.suite.Suite
import dev.koga.deeplinklauncher.cli.suite.SuiteCase
import dev.koga.deeplinklauncher.cli.suite.Verdicts
import java.io.File

internal class TestCommand(
    private val toolchain: () -> Toolchain,
    private val readStdin: () -> String = { System.`in`.readBytes().decodeToString() },
) : ReportingCommand<TestReport>(
    name = "test",
    summary = "Open every link of a suite on a device and check each against its expectation.",
    details = "The suite is a DeepLink Launcher export (JSON) or one link per line; pass - to read it from stdin. " +
        "In an export, targetPackage is the expected Android app, and an optional \"expect\" object sets " +
        "\"android\" (package or package/activity), \"ios\" (bundle id) and \"opens\" (false for links that " +
        "must not open any app). Without expectations a link passes when an app opens it and keeps running. " +
        "Exits with 1 when any link fails.",
    serializer = TestReport.serializer(),
) {
    private val source by argument("SUITE", help = "Path to the suite file, or - for stdin")
    private val folder by option("--folder", help = "Only run links from this folder of the export")
    private val target by TargetOptions()
    private val app by option(
        "--app",
        metavar = "BUNDLE_ID",
        help = "On physical iPhones, the app to open links in when a link has no expect.ios",
    )
    private val watchMs by option("--watch", metavar = "MS", help = "How long to watch each app after opening (default 1500)")
        .long()
        .restrictTo(min = 0)
        .default(DEFAULT_WATCH_MS)

    override fun execute(): Outcome<TestReport> {
        val cases = selected(Suite.parse(readSuite()))
        cases.forEach { requireLink(it.url) }
        val toolchain = toolchain()
        val device = target.select(toolchain)
        if (device.platform == Platform.IOS && !device.virtual) requireTargetApps(cases)
        val actions = LinkActions(toolchain)

        val results = cases.map { case ->
            val open = actions.open(device, case.url, watchMs, case.expect.ios ?: app)
            val verdict = Verdicts.judge(case, device.platform, open)
            TestReport.CaseResult(
                name = case.name,
                url = case.url,
                folder = case.folder,
                status = verdict.status,
                reason = verdict.reason,
                expected = TestReport.Expected(opens = case.expect.opens, handler = case.expectedHandler(device.platform)),
                actual = TestReport.Actual(status = open.status, handler = open.handler?.id, timeMs = open.timeMs, crash = open.crash),
            )
        }

        val report = TestReport(
            device = device,
            summary = TestReport.Summary(
                total = results.size,
                passed = results.count { it.status == TestStatus.PASSED },
                failed = results.count { it.status == TestStatus.FAILED },
                unverified = results.count { it.status == TestStatus.UNVERIFIED },
            ),
            results = results,
        )
        return Outcome(report, if (report.summary.failed > 0) ExitCode.FAILED else ExitCode.OK)
    }

    override fun render(report: TestReport): String {
        val nameWidth = report.results.maxOf { (it.name ?: it.url).length }
        val lines = report.results.joinToString("\n") { result ->
            val mark = when (result.status) {
                TestStatus.PASSED -> Text.OK
                TestStatus.FAILED -> Text.FAIL
                TestStatus.UNVERIFIED -> "?"
            }
            val label = (result.name ?: result.url).padEnd(nameWidth)
            val extra = listOfNotNull(
                result.url.takeIf { result.name != null && result.status != TestStatus.PASSED },
                result.actual.crash,
            )
            "$mark $label   ${result.detail()}".trimEnd() + extra.joinToString("") { "\n" + it.prependIndent("    ") }
        }
        val summary = report.summary
        val counts = listOfNotNull(
            "${summary.passed} passed",
            "${summary.failed} failed",
            "${summary.unverified} unverified".takeIf { summary.unverified > 0 },
        ).joinToString(", ")
        return "Ran ${Text.count(summary.total, "link")} on ${report.device.name} (${report.device.summary})\n" +
            "$lines\n\n$counts"
    }

    private fun readSuite(): String {
        if (source == "-") return readStdin()
        val file = File(source)
        if (!file.isFile) {
            throw CliFailure(ExitCode.USAGE, "Suite file not found: $source", "Pass a path to an exported JSON file, or - for stdin.")
        }
        return file.readText()
    }

    private fun selected(cases: List<SuiteCase>): List<SuiteCase> {
        val filtered = folder?.let { name -> cases.filter { it.folder.equals(name, ignoreCase = true) } } ?: cases
        if (filtered.isEmpty()) {
            val folders = cases.mapNotNull(SuiteCase::folder).distinct()
            throw CliFailure(
                exitCode = ExitCode.USAGE,
                message = if (folder == null) "The suite has no links." else "No links in folder \"$folder\".",
                hint = folders.takeIf { it.isNotEmpty() }?.joinToString(prefix = "Folders in the suite: "),
            )
        }
        return filtered
    }

    private fun TestReport.CaseResult.detail(): String = when {
        reason != null -> reason
        !expected.opens -> "not handled, as expected"
        else -> listOfNotNull(actual.handler, actual.timeMs?.let { "$it ms" }).joinToString(" · ").ifEmpty { "opened" }
    }

    private fun requireTargetApps(cases: List<SuiteCase>) {
        val missing = cases.filter { it.expect.ios == null && app == null }
        if (missing.isNotEmpty()) {
            throw CliFailure(
                exitCode = ExitCode.USAGE,
                message = "Physical iPhones open links inside a given app, and ${Text.count(missing.size, "link")} " +
                    "${if (missing.size == 1) "has" else "have"} none.",
                hint = "Pass --app <bundle id>, or set \"expect\": { \"ios\": \"<bundle id>\" } on: " +
                    missing.joinToString { it.name ?: it.url },
            )
        }
    }

    private fun SuiteCase.expectedHandler(platform: Platform): String? = when (platform) {
        Platform.ANDROID -> expect.android
        Platform.IOS -> expect.ios
    }

    private companion object {
        const val DEFAULT_WATCH_MS = 1500L
    }
}
