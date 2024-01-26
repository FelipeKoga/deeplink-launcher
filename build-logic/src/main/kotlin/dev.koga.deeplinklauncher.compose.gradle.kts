import extension.composeConfig
import extension.getBundle
import extension.getLibrary
import gradle.kotlin.dsl.accessors._4a1608c7e4c1251e896c27efcc41b09e.android
import gradle.kotlin.dsl.accessors._4a1608c7e4c1251e896c27efcc41b09e.api
import gradle.kotlin.dsl.accessors._4a1608c7e4c1251e896c27efcc41b09e.debugApi

plugins {
    id("dev.koga.deeplinklauncher.multiplataform")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    composeConfig(libs)
}

dependencies {

    api(libs.getBundle("compose"))
    api(libs.getLibrary("core.ktx"))

    debugApi(libs.getLibrary("compose.tooling"))
}
