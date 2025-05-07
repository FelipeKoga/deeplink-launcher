plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlinx.datetime)
    implementation(libs.detekt)
}
