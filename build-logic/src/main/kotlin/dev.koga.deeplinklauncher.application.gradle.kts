import extension.composeConfig
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.koga.deeplinklauncher.code-analysis")
}

val keystoreProperties = Properties()
if (rootDir.resolve("keystore.properties").exists()) {
    keystoreProperties.load(File(rootDir, "keystore.properties").inputStream())
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {

    defaultConfig.targetSdk = Configuration.targetSdk
    defaultConfig {
        compileSdk = Configuration.compileSdk
        minSdk = Configuration.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionName = Configuration.versionName
        versionCode = Configuration.versionCode
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Configuration.jvmTarget
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    composeConfig(libs)

    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = keystoreProperties.getProperty("KEYSTORE_PASSWORD", "")
            keyAlias = keystoreProperties.getProperty("KEYSTORE_ALIAS", "")
            keyPassword = keystoreProperties.getProperty("KEY_PASSWORD", "")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
