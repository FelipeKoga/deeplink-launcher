package dev.koga.deeplinklauncher.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import dev.koga.deeplinklauncher.cli.command.DevicesCommand
import dev.koga.deeplinklauncher.cli.command.DoctorCommand
import dev.koga.deeplinklauncher.cli.command.OpenCommand
import dev.koga.deeplinklauncher.cli.command.ParseCommand
import dev.koga.deeplinklauncher.cli.command.ResolveCommand
import dev.koga.deeplinklauncher.cli.command.TestCommand
import kotlin.system.exitProcess

internal fun deeplinkCli(toolchain: () -> Toolchain = Toolchain::system): CliktCommand =
    DeeplinkCommand().subcommands(
        DoctorCommand(toolchain),
        DevicesCommand(toolchain),
        OpenCommand(toolchain),
        ResolveCommand(toolchain),
        TestCommand(toolchain),
        ParseCommand(),
    )

internal fun exitCodeOf(error: CliktError): Int = when (error) {
    is ProgramResult -> error.statusCode
    is PrintMessage -> if (error.statusCode == 0) ExitCode.OK.code else ExitCode.USAGE.code
    else -> if (error.statusCode == 0) ExitCode.OK.code else ExitCode.USAGE.code
}

fun main(args: Array<String>) {
    val cli = deeplinkCli()
    val exitCode = try {
        cli.parse(args)
        ExitCode.OK.code
    } catch (error: CliktError) {
        cli.echoFormattedHelp(error)
        exitCodeOf(error)
    }
    exitProcess(exitCode)
}
