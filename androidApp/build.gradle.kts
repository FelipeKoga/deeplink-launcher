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

    implementation(libs.splashscreen)
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

fun getSigningKey(secretKey: String, fallbackProps: Properties): String =
    if (!System.getenv(secretKey).isNullOrEmpty()) {
        System.getenv(secretKey)
    } else {
        fallbackProps.getProperty(secretKey)
    }
