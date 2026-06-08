package dev.koga.deeplinklauncher.platform

import java.io.File

private const val APP_NAME_DISPLAY = "DeepLink Launcher"
private const val APP_NAME_LINUX = "deeplink-launcher"

object JvmAppDataDirectory {
    fun resolve(): File = resolveAppDataDirectory(
        userHome = System.getProperty("user.home"),
        osName = System.getProperty("os.name"),
        appDataEnv = System.getenv("APPDATA"),
        xdgDataHome = System.getenv("XDG_DATA_HOME"),
    )
}

internal fun resolveAppDataDirectory(
    userHome: String,
    osName: String,
    appDataEnv: String?,
    xdgDataHome: String?,
): File {
    val os = osName.lowercase()
    val dir = when {
        "mac" in os || "darwin" in os ->
            File(userHome, "Library/Application Support/$APP_NAME_DISPLAY")

        "win" in os ->
            File(appDataEnv ?: userHome, APP_NAME_DISPLAY)

        else -> {
            val dataHome = xdgDataHome?.let(::File)
                ?: File(userHome, ".local/share")
            File(dataHome, APP_NAME_LINUX)
        }
    }
    dir.mkdirs()
    return dir
}
