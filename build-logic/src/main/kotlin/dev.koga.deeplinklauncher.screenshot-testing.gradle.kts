import extension.libs

plugins {
    id("dev.koga.deeplinklauncher.compose-multiplatform")
}

apply(plugin = "io.github.takahirom.roborazzi")

val previewPackage = "dev.koga.deeplinklauncher." +
    path.split(":").drop(2).joinToString(".") { it.replace("-", "") }

kotlin {
    sourceSets {
        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
            implementation(libs.roborazzi.previewScannerSupport)
            implementation(libs.composablePreviewScanner.android)
            implementation(libs.compose.ui.test.junit4)
        }
    }
}

dependencies {
    "debugImplementation"(libs.compose.ui.test.manifest)
    "debugImplementation"(libs.roborazzi.annotations)
}

android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                it.systemProperties["user.timezone"] = "UTC"
                it.jvmArgs("-Duser.language=en", "-Duser.country=US")
                it.maxHeapSize = "2g"
                if (providers.gradleProperty("excludeScreenshotTests").isPresent) {
                    it.useJUnit {
                        excludeCategories("com.github.takahirom.roborazzi.RoborazziComposePreviewTestCategory")
                    }
                }
            }
        }
    }
}

extensions.getByName("roborazzi")
    .withGroovyBuilder { getProperty("generateComposePreviewRobolectricTests") }
    .withGroovyBuilder {
        @Suppress("UNCHECKED_CAST")
        (getProperty("enable") as Property<Boolean>).set(true)
        @Suppress("UNCHECKED_CAST")
        (getProperty("packages") as ListProperty<String>).set(listOf(previewPackage))
        @Suppress("UNCHECKED_CAST")
        (getProperty("includePrivatePreviews") as Property<Boolean>).set(true)
        @Suppress("UNCHECKED_CAST")
        (getProperty("robolectricConfig") as MapProperty<String, String>).set(
            mapOf("sdk" to "[35]", "qualifiers" to "RobolectricDeviceQualifiers.Pixel5"),
        )
    }
