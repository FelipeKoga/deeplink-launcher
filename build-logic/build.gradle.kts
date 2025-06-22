plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    google()
}

dependencies {

    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlinx.datetime)
    implementation(libs.detekt)
    implementation(libs.compose.compiler.plugin)
    implementation(libs.compose.gradlePlugin)

    implementation(files(libs::class.java.superclass.protectionDomain.codeSource.location))
}
