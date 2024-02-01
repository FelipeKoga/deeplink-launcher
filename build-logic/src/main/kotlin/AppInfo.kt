import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Configuration {
    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 4

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
        val instant: Instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val year = localDateTime.year % 100
        val month = localDateTime.monthNumber
        val day = localDateTime.dayOfMonth
        val hour = localDateTime.hour

        return year * 1000000 + month * 10000 + day * 100 + hour
    }
}