package dev.koga.deeplinklauncher.cli.command

import dev.koga.deeplinklauncher.cli.output.Handler

internal object Text {
    const val OK = "✓"
    const val FAIL = "✗"

    fun failure(message: String, hint: String?): String =
        listOfNotNull("$FAIL $message", hint?.prependIndent("  ")).joinToString("\n")

    fun count(n: Int, noun: String): String = "$n $noun${if (n == 1) "" else "s"}"

    fun fields(vararg rows: Pair<String, String?>): String {
        val present = rows.filter { it.second != null }
        val width = present.maxOfOrNull { it.first.length } ?: 0
        return present.joinToString("\n") { (label, value) ->
            "  ${label.padEnd(width)}   ${value!!.replace("\n", "\n" + " ".repeat(width + 5))}"
        }
    }
}

internal fun Handler.label(): String = if (name != null) "$name ($id)" else id
