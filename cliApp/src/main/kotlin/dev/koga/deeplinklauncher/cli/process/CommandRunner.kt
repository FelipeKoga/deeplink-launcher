package dev.koga.deeplinklauncher.cli.process

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class CommandResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
) {
    val succeeded: Boolean get() = exitCode == 0
}

internal interface CommandRunner {
    fun run(command: List<String>, input: String? = null): CommandResult
}

internal class ProcessCommandRunner(
    private val timeout: Duration = 15.seconds,
) : CommandRunner {

    override fun run(command: List<String>, input: String?): CommandResult {
        val process = try {
            ProcessBuilder(command).start()
        } catch (e: IOException) {
            throw CliFailure(ExitCode.TOOLING, "Could not run ${command.first()}: ${e.message}", cause = e)
        }

        var stdout = ""
        var stderr = ""
        val readers = listOf(
            thread { stdout = process.inputStream.bufferedReader().readText() },
            thread { stderr = process.errorStream.bufferedReader().readText() },
        )

        process.outputStream.use { stream -> input?.let { stream.write(it.toByteArray()) } }

        if (!process.waitFor(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)) {
            process.destroyForcibly()
            throw CliFailure(
                exitCode = ExitCode.DEVICE,
                message = "${command.take(4).joinToString(" ")} did not finish within $timeout",
                hint = "Check that the device is responsive, then retry.",
            )
        }

        readers.forEach(Thread::join)
        return CommandResult(process.exitValue(), stdout, stderr)
    }
}
