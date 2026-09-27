plugins {
    id("codeanalysis.detekt")
    id("codeanalysis.ktlint")
}

tasks.named("check") {
    dependsOn("ktlint", "detekt")
}
