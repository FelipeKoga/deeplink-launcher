package extension

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