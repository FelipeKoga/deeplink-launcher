import kotlinx.datetime.Clock
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.koga.deeplinklauncher.code-analysis")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.aboutlibraries)
}

val keystoreProperties = Properties()
if (rootDir.resolve("keystore.properties").exists()) {
    keystoreProperties.load(File(rootDir, "keystore.properties").inputStream())
}

android {
    namespace = "dev.koga.deeplinklauncher.android"

    defaultConfig.targetSdk = libs.versions.androidApp.targetSdk.get().toInt()
    defaultConfig {
        compileSdk = libs.versions.androidApp.compileSdk.get().toInt()
        minSdk = libs.versions.androidApp.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionName = getVersionName()
        versionCode = getVersionCode()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(getSigningKey("KEYSTORE_FILE_NAME", keystoreProperties))
            storePassword = getSigningKey("KEYSTORE_PASSWORD", keystoreProperties)
            keyAlias = getSigningKey("KEYSTORE_ALIAS", keystoreProperties)
            keyPassword = getSigningKey("KEY_PASSWORD", keystoreProperties)
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

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.billing)
    implementation(libs.revenuecat.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.profileinstaller)

    baselineProfile(projects.baselineprofile)
}

fun getVersionCode(): Int {
    val major = libs.versions.app.major.get().toInt()
    val minor = libs.versions.app.minor.get().toInt()
    val patch = libs.versions.app.patch.get().toInt()

    return (major * 10000) +
            (minor * 100) +
            patch +
            Clock.System.now().epochSeconds.toInt()
}

fun getVersionName(): String {
    return "${libs.versions.app.major.get()}.${libs.versions.app.minor.get()}.${libs.versions.app.patch.get()}"
}

fun getSigningKey(secretKey: String, fallbackProps: Properties): String =
    if (!System.getenv(secretKey).isNullOrEmpty()) {
        System.getenv(secretKey)
    } else {
        fallbackProps.getProperty(secretKey)
    }
