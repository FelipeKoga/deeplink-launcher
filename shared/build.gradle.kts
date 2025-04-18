import extension.setupBinariesFramework
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework(name = "shared", isStatic = true)

    task("testClasses")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.ui)
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.deeplink.impl)
            implementation(projects.feature.importExport.ui)
            implementation(projects.feature.importExport.api)
            implementation(projects.feature.importExport.impl)
            api(projects.feature.preferences.api)
            implementation(projects.feature.preferences.impl)
            implementation(projects.feature.settings.ui)
            implementation(projects.feature.home.ui)
            implementation(projects.feature.purchase.ui)
            api(projects.feature.purchase.api)
            implementation(projects.feature.purchase.impl)

            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
            implementation(projects.core.file)
            implementation(projects.core.date)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.voyager.bottomSheet)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.impl)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.shared"
}

project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}