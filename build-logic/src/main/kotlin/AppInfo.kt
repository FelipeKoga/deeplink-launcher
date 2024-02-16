import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneOffset

object Configuration {
    private const val majorVersion = 1
    private const val minorVersion = 1
    private const val patchVersion = 3

    const val versionName = "$majorVersion.$minorVersion.$patchVersion"
    val versionCode = (majorVersion * 10000) +
            (minorVersion * 100) +
            patchVersion +
            getDateVersionBuild()

    const val targetSdk = 34
    const val compileSdk = 34
    const val minSdk = 26
    const val jvmTarget = "1.8"

    private fun getDateVersionBuild(): Int {
        return Clock.System.now().epochSeconds.toInt()
    }
}