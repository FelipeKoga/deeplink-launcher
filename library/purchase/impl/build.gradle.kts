import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import extension.envProperties


plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
    id("com.codingfeline.buildkonfig")
}

val envProperties = rootProject.envProperties()

kotlin {
    sourceSets {
        val noOpMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain.get().dependsOn(noOpMain)
        iosMain.get().dependsOn(noOpMain)

        commonMain.dependencies {
            implementation(projects.library.purchase.api)

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
