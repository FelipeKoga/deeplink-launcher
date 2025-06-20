package extension

import org.gradle.api.Project
import java.util.Properties

fun Project.envProperties(): Properties {
    val properties = Properties()

    if (file("env.properties").exists()) {
        properties.load(
            file("env.properties").inputStream(),
        )
    }

    return properties
}