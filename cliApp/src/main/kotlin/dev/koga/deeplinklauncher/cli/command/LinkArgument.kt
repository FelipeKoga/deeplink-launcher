package dev.koga.deeplinklauncher.cli.command

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import dev.koga.deeplinklauncher.cli.link.ParsedLink

internal fun requireLink(url: String): String {
    if (!ParsedLink(url).isValid) {
        throw CliFailure(
            exitCode = ExitCode.USAGE,
            message = "Not a link: $url",
            hint = "A link needs a scheme, like myapp://product/42 or https://example.com/promo.",
        )
    }
    return url
}
