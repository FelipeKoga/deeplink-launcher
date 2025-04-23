package dev.koga.deeplinklauncher.preferences.model

public enum class AppTheme {
    LIGHT,
    DARK,
    AUTO,
    ;

    public companion object {
        public fun get(name: String?): AppTheme {
            return entries.firstOrNull { it.name == name } ?: AUTO
        }
    }
}
