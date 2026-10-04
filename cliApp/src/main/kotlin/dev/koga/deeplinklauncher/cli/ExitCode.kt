package dev.koga.deeplinklauncher.cli

internal enum class ExitCode(val code: Int, val meaning: String) {
    OK(0, "success"),
    FAILED(1, "the link was not handled, the app crashed, or a check failed"),
    USAGE(2, "invalid arguments, or text that is not a link"),
    DEVICE(3, "no usable device, an ambiguous device, or a device that did not respond"),
    TOOLING(4, "adb or xcrun is missing"),
}

internal class CliFailure(
    val exitCode: ExitCode,
    override val message: String,
    val hint: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)
