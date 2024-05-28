package extension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.binariesFrameworkConfig(name: String) {
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = name
//            isStatic = true
//        }
//    }
}

fun KotlinMultiplatformExtension.addCompose() {
}