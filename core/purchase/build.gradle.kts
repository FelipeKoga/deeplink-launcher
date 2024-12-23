import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import extension.envProperties
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    id("com.codingfeline.buildkonfig")
}

val envProperties = rootProject.envProperties()

kotlin {
    setupBinariesFramework("purchase")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}

buildkonfig {
    packageName = "dev.koga.shared"

    defaultConfigs {
        buildConfigField(
            STRING,
            "REVENUECAT_API_KEY",
            envProperties.getProperty("REVENUECAT_API_KEY")
        )
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.purchase"
}
