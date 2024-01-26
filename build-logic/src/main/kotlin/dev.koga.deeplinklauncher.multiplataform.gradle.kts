//import gradle.kotlin.dsl.accessors._4a1608c7e4c1251e896c27efcc41b09e.android
//
//plugins {
//    kotlin("multiplatform")
//    id("com.android.library")
//}
//
//kotlin {
//    applyDefaultHierarchyTemplate()
//
//    androidTarget {
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = Configuration.jvmTarget
//            }
//        }
//    }
//}
//
//val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
//
//android {
//    compileSdk = Configuration.compileSdk
//    defaultConfig {
//        minSdk = Configuration.minSdk
//    }
//}
//
