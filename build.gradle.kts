buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.androidLibrary) apply(false)
    alias(libs.plugins.kotlinAndroid) apply(false)
    alias(libs.plugins.kotlinMultiplatform) apply(false)
    alias(libs.plugins.sqlDelight) apply(false)
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply(false)
    alias(libs.plugins.jetbrainsCompose) apply(false)
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
}