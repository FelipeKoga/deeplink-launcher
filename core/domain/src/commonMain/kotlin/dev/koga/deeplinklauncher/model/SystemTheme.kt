package dev.koga.deeplinklauncher.model

enum class SystemTheme {
    LIGHT,
    DARK,
    AUTO;

    companion object {
        fun get(name: String?): SystemTheme {
            return entries.firstOrNull { it.name == name } ?: AUTO
        }
    }
}