package extension

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import java.util.Properties
import org.gradle.accessors.dm.LibrariesForLibs

val Project.libs
    get() = the<LibrariesForLibs>()

fun Project.envProperties(): Properties {
    val properties = Properties()

    if (file("env.properties").exists()) {
        properties.load(
            file("env.properties").inputStream(),
        )
    }

    return properties
}

fun Project.getVersionName(): String {
    val patchVersion = libs.versions.android.patchVersion
    val minorVersion = libs.versions.android.minorVersion
    val majorVersion = libs.versions.android.majorVersion

    return "$majorVersion.$minorVersion.$patchVersion"
}

fun Project.getAndroidVersionCode(): Int {
    // Latest version code on Google Play
    val versionCodeBase = 1_800_000_000

    val patchVersion = libs.versions.android.patchVersion.get().toInt()
    val minorVersion = libs.versions.android.minorVersion.get().toInt()
    val majorVersion = libs.versions.android.majorVersion.get().toInt()
    val dateCode = getDateCode()

    val versionCode = (majorVersion * 10_000) +
            (minorVersion * 100) +
            patchVersion +
            dateCode +
            versionCodeBase

    require(versionCode < Int.MAX_VALUE)

    return versionCode
}

private fun getDateCode(): Int {
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val year = today.year % 100
    val month = today.month.number
    val day = today.day
    return year * 10_000 + month * 100 + day
}