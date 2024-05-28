import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.koga.deeplinklauncher.code-analysis")
    id("com.mikepenz.aboutlibraries.plugin")
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.compose.compiler)
}

val keystoreProperties = Properties()
if (rootDir.resolve("keystore.properties").exists()) {
    keystoreProperties.load(File(rootDir, "keystore.properties").inputStream())
}

android {
    namespace = "dev.koga.deeplinklauncher.android"

    defaultConfig.targetSdk = Configuration.TARGET_SDK
    defaultConfig {
        compileSdk = Configuration.COMPILE_SDK
        minSdk = Configuration.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionName = Configuration.VERSION_NAME
        versionCode = Configuration.versionCode
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Configuration.JVM_TARGET
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
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

dependencies {
    implementation(projects.shared)
    implementation(projects.core.preferences)

    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.compose.runtime)
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn("exportLibraryDefinitions")
}

fun getSigningKey(secretKey: String, fallbackProps: Properties): String =
    if (!System.getenv(secretKey).isNullOrEmpty()) {
        System.getenv(secretKey)
    } else {
        fallbackProps.getProperty(secretKey)
    }
