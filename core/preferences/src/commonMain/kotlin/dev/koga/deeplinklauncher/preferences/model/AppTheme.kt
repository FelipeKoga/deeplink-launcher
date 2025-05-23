package dev.koga.deeplinklauncher.preferences.model

enum class AppTheme {
    LIGHT,
    DARK,
    AUTO,
    ;

    companion object {
        fun get(name: String?): AppTheme {
            return entries.firstOrNull { it.name == name } ?: AUTO
        }
    }
}
