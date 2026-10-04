package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.output.ErrorReport
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal data class Outcome<R>(val report: R, val exitCode: ExitCode = ExitCode.OK)

internal abstract class ReportingCommand<R>(
    name: String,
    private val summary: String,
    private val details: String,
    private val serializer: KSerializer<R>,
) : CliktCommand(name) {

    private val json by option("--json", help = "Print one JSON object on stdout instead of text").flag()

    override fun help(context: Context): String = "$summary\n\n$details"

    override fun helpEpilog(context: Context): String = EXIT_CODES_HELP

    protected abstract fun execute(): Outcome<R>

    protected abstract fun render(report: R): String

    override fun run() {
        val exitCode = try {
            val outcome = execute()
            echo(if (json) JSON.encodeToString(serializer, outcome.report) else render(outcome.report))
            outcome.exitCode
        } catch (failure: CliFailure) {
            if (json) {
                echo(JSON.encodeToString(ErrorReport.serializer(), failure.toReport()))
            } else {
                echo(Text.failure(failure.message, failure.hint), err = true)
            }
            failure.exitCode
        }
        if (exitCode != ExitCode.OK) throw ProgramResult(exitCode.code)
    }

    private fun CliFailure.toReport() = ErrorReport(
        command = commandName,
        error = ErrorReport.ErrorBody(exitCode = exitCode.code, message = message, hint = hint),
    )

    companion object {
        val JSON = Json {
            prettyPrint = true
            encodeDefaults = true
        }

        val EXIT_CODES_HELP = "Exit codes:\n\n" +
            ExitCode.entries.joinToString("\u0085") { "\u00A0\u00A0${it.code}\u00A0\u00A0${it.meaning}" }
    }
}
