package extension

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Properties

fun CommonExtension<*, *, *, *, *>.composeConfig(libs: VersionCatalog) {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.getVersion("compose-compiler").toString()
    }

    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

internal fun CommonExtension<*, *, *, *, *>.setupReleaseSigningConfig(
    project: Project,
) {
    val file = project.rootProject.file("keystore.properties")

    val keystoreProperties = Properties()
    if (file.exists()) {
        keystoreProperties.load(file.inputStream())
    }

    signingConfigs {
        create("release") {
            storeFile = project.file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword", "")
            keyAlias = keystoreProperties.getProperty("keyAlias", "")
            keyPassword = keystoreProperties.getProperty("keyPassword", "")
        }
    }
}

fun KotlinMultiplatformExtension.binariesFrameworkConfig(name: String) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = name
            isStatic = true
        }
    }
}