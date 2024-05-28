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
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.aboutlibraries.plugin) apply false
}