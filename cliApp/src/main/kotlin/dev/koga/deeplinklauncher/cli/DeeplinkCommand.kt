package dev.koga.deeplinklauncher.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.versionOption
import dev.koga.deeplinklauncher.cli.command.ReportingCommand

internal class DeeplinkCommand : CliktCommand(name = "deeplink") {

    override val printHelpOnEmptyArgs = true

    init {
        versionOption(DeeplinkCommand::class.java.`package`?.implementationVersion ?: "dev")
    }

    override fun help(context: Context): String =
        "Open and inspect deeplinks on Android devices and iOS simulators. " +
            "Built for scripts and AI agents: every command takes --json, never prompts, " +
            "and exits with a code that tells what happened."

    override fun helpEpilog(context: Context): String =
        "Examples:\n\n" + listOf(
            "deeplink doctor",
            "deeplink open \"myapp://product/42?ref=email\"",
            "deeplink resolve \"https://example.com/promo\" --platform android --json",
        ).joinToString("\u0085") { "\u00A0\u00A0$it" } + "\n\n" + ReportingCommand.EXIT_CODES_HELP

    override fun run() = Unit
}
