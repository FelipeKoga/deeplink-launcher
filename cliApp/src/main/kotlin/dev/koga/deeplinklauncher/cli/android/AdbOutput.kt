package dev.koga.deeplinklauncher.cli.android

internal data class AdbDeviceLine(
    val serial: String,
    val state: String,
    val model: String?,
)

internal data class Component(val packageName: String, val className: String) {
    val flattened: String get() = "$packageName/$className"

    val isChooser: Boolean get() = className.endsWith(".ResolverActivity") || className.endsWith(".ChooserActivity")

    companion object {
        private val pattern = Regex("""^([A-Za-z][\w.]*)/([\w.$]+)$""")

        fun parse(text: String): Component? =
            pattern.matchEntire(text.trim())?.destructured?.let { (pkg, cls) -> Component(pkg, cls) }
    }
}

internal data class AmStart(
    val status: String?,
    val activity: Component?,
    val launchState: String?,
    val totalTimeMs: Long?,
    val error: String?,
    val warning: String?,
)

internal object AdbOutput {

    fun devices(text: String): List<AdbDeviceLine> =
        text.lineSequence()
            .drop(1)
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { line ->
                val parts = line.split(Regex("\\s+"))
                AdbDeviceLine(
                    serial = parts[0],
                    state = parts.getOrElse(1) { "unknown" },
                    model = parts.firstOrNull { it.startsWith("model:") }?.removePrefix("model:"),
                )
            }
            .toList()

    fun amStart(text: String): AmStart {
        val fields = text.lineSequence()
            .mapNotNull { line ->
                val colon = line.indexOf(':').takeIf { it > 0 } ?: return@mapNotNull null
                line.substring(0, colon).trim() to line.substring(colon + 1).trim()
            }
            .toList()
        fun field(name: String) = fields.firstOrNull { it.first == name }?.second

        return AmStart(
            status = field("Status"),
            activity = field("Activity")?.let(Component::parse),
            launchState = field("LaunchState"),
            totalTimeMs = field("TotalTime")?.toLongOrNull(),
            error = field("Error"),
            warning = field("Warning"),
        )
    }

    fun components(text: String): List<Component> =
        text.lineSequence().mapNotNull(Component::parse).toList()
}
