import extension.composeConfig
import extension.getBundle
import extension.getPlugin
import extension.proguardConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply(
    libs.getPlugin("jetbrainsCompose"),
)

android {
    composeConfig(libs)
}

dependencies {
    implementation(libs.getBundle("compose"))
}
