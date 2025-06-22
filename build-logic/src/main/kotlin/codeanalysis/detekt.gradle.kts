package codeanalysis

import extension.libs
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(libs.composerules)
}

configure<DetektExtension> {
    config = files("$rootDir/config/filters/detekt.yml")
    allRules = true
    baseline = file("$rootDir/detekt-baseline.xml")
}
