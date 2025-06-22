plugins {
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.androidLibrary) apply(false)
    alias(libs.plugins.kotlinAndroid) apply(false)
    alias(libs.plugins.kotlinMultiplatform) apply(false)
    alias(libs.plugins.sqlDelight) apply(false)
    alias(libs.plugins.kotlinJvm) apply(false)
    alias(libs.plugins.composeMultiplatform) apply(false)
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.firebaseCrashlytics) apply false
    alias(libs.plugins.firebasePerf) apply false
    alias(libs.plugins.aboutLibraries) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.baselineProfile) apply false
}

buildscript {
    dependencies {
        classpath(libs.build.konfig)
    }
}