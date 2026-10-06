import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.deeplinkLauncher.metro)
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
            implementation(projects.feature.home.api)
            implementation(projects.feature.deeplink.impl)
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.dataTransfer.impl)
            implementation(projects.feature.dataTransfer.api)
            implementation(projects.feature.settings.impl)
            implementation(projects.feature.settings.api)
            implementation(projects.library.purchase.api)
            implementation(projects.library.purchase.impl)
            implementation(projects.library.analytics.api)
            implementation(projects.library.analytics.impl)

            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
            implementation(projects.core.file)
            implementation(projects.core.date)
            implementation(projects.core.coroutines)
            implementation(projects.core.uiEvent)
            implementation(projects.core.preferences)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.metrox.viewmodel.compose)
            implementation(libs.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)
        }

        jvmMain.dependencies {
            implementation(projects.library.deviceBridge.api)
            implementation(projects.library.deviceBridge.impl)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
        }
    }
}

project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}