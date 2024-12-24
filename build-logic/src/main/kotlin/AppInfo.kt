import kotlinx.datetime.Clock

object AndroidAppConfiguration {
    private const val MAJOR_VERSION = 1
    private const val MINOR_VERSION = 7
    private const val PATCH_VERSION = 4

    const val VERSION_NAME = "$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION"
    val versionCode = (MAJOR_VERSION * 10000) +
            (MINOR_VERSION * 100) +
            PATCH_VERSION +
            getDateVersionBuild()

    const val TARGET_SDK = 34
    const val COMPILE_SDK = 34
    const val MIN_SDK = 26

    private fun getDateVersionBuild(): Int {
        return Clock.System.now().epochSeconds.toInt()
    }
}

object DesktopAppConfiguration {
    const val VERSION_NAME = "1.1.2"
}