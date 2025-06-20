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
    setupBinariesFramework("purchase.impl")

    sourceSets {
        val noOpMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain.get().dependsOn(noOpMain)
        iosMain.get().dependsOn(noOpMain)

        commonMain.dependencies {
            implementation(projects.feature.purchase.api)

            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.revenuecat.core)
        }
    }
}

buildkonfig {
    packageName = "dev.koga.deeplinklauncher.purchase.impl"

    defaultConfigs {
        buildConfigField(
            STRING,
            "REVENUECAT_API_KEY",
            envProperties.getProperty("REVENUECAT_API_KEY")
        )
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.purchase.impl"
}
