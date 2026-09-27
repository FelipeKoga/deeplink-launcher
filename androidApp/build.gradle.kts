import extension.getAndroidVersionCode
import extension.getVersionName
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.firebasePerf)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.baselineProfile)
    alias(libs.plugins.aboutLibraries)
}

val keystoreProperties = Properties()
if (rootDir.resolve("keystore.properties").exists()) {
    keystoreProperties.load(File(rootDir, "keystore.properties").inputStream())
}



android {
    namespace = "dev.koga.deeplinklauncher.android"

    defaultConfig.targetSdk = libs.versions.android.targetSdk.get().toInt()
    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionName = getVersionName()
        versionCode = getAndroidVersionCode()
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

    // Release signing is optional so that debug builds, tests and code analysis
    // configure without secrets. Release builds stay unsigned when keys are missing.
    val releaseSigningKeys = listOf("KEYSTORE_FILE_NAME", "KEYSTORE_PASSWORD", "KEYSTORE_ALIAS", "KEY_PASSWORD")
        .associateWith { getSigningKey(it, keystoreProperties) }

    signingConfigs {
        if (releaseSigningKeys.values.all { !it.isNullOrEmpty() }) {
            create("release") {
                storeFile = file(releaseSigningKeys.getValue("KEYSTORE_FILE_NAME")!!)
                storePassword = releaseSigningKeys.getValue("KEYSTORE_PASSWORD")
                keyAlias = releaseSigningKeys.getValue("KEYSTORE_ALIAS")
                keyPassword = releaseSigningKeys.getValue("KEY_PASSWORD")
            }
        } else {
            logger.info("Release signing keys not found; release builds will be unsigned.")
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
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.shared)

    implementation(libs.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.billing)
    implementation(libs.revenuecat.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.profileinstaller)

    baselineProfile(projects.baselineprofile)
}

fun getSigningKey(secretKey: String, fallbackProps: Properties): String? =
    if (!System.getenv(secretKey).isNullOrEmpty()) {
        System.getenv(secretKey)
    } else {
        fallbackProps.getProperty(secretKey)
    }
