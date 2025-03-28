plugins {
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.androidLibrary) apply(false)
    alias(libs.plugins.kotlinAndroid) apply(false)
    alias(libs.plugins.kotlinMultiplatform) apply(false)
    alias(libs.plugins.sqlDelight) apply(false)
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply(false)
    alias(libs.plugins.jetbrainsCompose) apply(false)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.aboutlibraries.plugin) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
}

buildscript {
    dependencies {
        classpath(libs.build.konfig)
    }
}