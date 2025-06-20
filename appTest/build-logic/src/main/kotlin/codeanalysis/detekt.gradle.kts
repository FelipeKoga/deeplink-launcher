package codeanalysis

import extension.getLibrary
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(libs.getLibrary("composerules"))
}

configure<DetektExtension> {
    config = files("$rootDir/config/filters/detekt.yml")
    allRules = true
    baseline = file("$rootDir/detekt-baseline.xml")
}
