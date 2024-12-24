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
            implementation(projects.feature.home)
            implementation(projects.feature.settings)
            implementation(projects.feature.exportData)
            implementation(projects.feature.importData)
            implementation(projects.feature.deeplinkDetails)
            implementation(projects.feature.folderDetails)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
            api(projects.core.purchase)
            api(projects.core.domain)
            api(projects.core.preferences)

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
    }
}

android {
    namespace = "dev.koga.shared"
}

project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}