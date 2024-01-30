plugins {
    id("codeanalysis.detekt")
    id("codeanalysis.ktlint")
}

tasks.getByName("check") {
    setDependsOn(
        listOf(
            tasks.getByName("ktlint"),
            tasks.getByName("detekt")
        )
    )
}
