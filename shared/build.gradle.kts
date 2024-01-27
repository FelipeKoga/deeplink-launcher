import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home)
            implementation(projects.feature.import)
            implementation(projects.feature.export)
            implementation(projects.feature.deeplinkDetails)
            implementation(projects.feature.folderDetails)

            api(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.koin.compose)

            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation(libs.voyager.koin)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "dev.koga.shared"
}