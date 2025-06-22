import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            this.baseName = "shared"
            this.isStatic = false
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home.impl)
            implementation(projects.feature.deeplink.impl)
            implementation(projects.feature.dataTransfer.impl)
            implementation(projects.feature.settings.impl)
            implementation(projects.feature.purchase.api)
            implementation(projects.feature.purchase.impl)

            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
            implementation(projects.core.file)
            implementation(projects.core.date)
            implementation(projects.core.coroutines)
            implementation(projects.core.uiEvent)
            implementation(projects.core.preferences)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.compose.navigation)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.impl)
        }
    }
}

project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}