

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home.ui)
            implementation(projects.feature.home.domain)
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.ui)
            implementation(projects.feature.exportDeeplinks.domain)
            implementation(projects.feature.exportDeeplinks.ui)
            implementation(projects.feature.importDeeplinks.domain)
            implementation(projects.feature.importDeeplinks.ui)
            implementation(projects.feature.deeplinkDetails.ui)
            implementation(projects.feature.folderDetails.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
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

