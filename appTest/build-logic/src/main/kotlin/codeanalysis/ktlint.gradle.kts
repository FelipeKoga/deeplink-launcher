package codeanalysis

import extension.getLibrary
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

val ktlint: Configuration by configurations.creating
val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    ktlint(libs.getLibrary("ktlint"))
}

tasks {
    register<JavaExec>("ktlint") {
        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        val buildDir = layout.buildDirectory.get().asFile.path
        args(
            "src/**/*.kt", "--reporter=plain", "--reporter=checkstyle," +
                    "output=${buildDir}/reports/ktlint.xml"
        )
    }

    register<JavaExec>("ktlintFormat") {
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("-F", "src/**/*.kt")
        jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
    }
}