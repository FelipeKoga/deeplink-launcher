plugins {
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.androidLibrary) apply(false)
    alias(libs.plugins.kotlinAndroid) apply(false)
    alias(libs.plugins.kotlinMultiplatform) apply(false)
    alias(libs.plugins.sqlDelight) apply(false)
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply(false)
    alias(libs.plugins.jetbrainsCompose) apply(false)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
//    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.aboutlibraries.plugin) apply false

    id(libs.plugins.modulegraph.get().pluginId) version libs.versions.modulegraph
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
    // showFullPath.set(false) // optional
    // orientation.set(Orientation.LEFT_TO_RIGHT) //optional
    // linkText.set(LinkText.NONE) // optional
    // setStyleByModuleType.set(true) // optional
    // excludedConfigurationsRegex.set(".*test.*") // optional
    // excludedModulesRegex.set(".*moduleName.*") // optional
    // focusedModulesRegex.set(".*(projectName).*") // optional
    // rootModulesRegex.set(".*moduleName.*") // optional
    // theme.set(Theme.NEUTRAL) // optional
    // or you can fully customize it by using the BASE theme:
    // Theme.BASE(
    //     themeVariables = mapOf(
    //         "primaryTextColor" to "#F6F8FAff", // Text
    //         "primaryColor" to "#5a4f7c", // Node
    //         "primaryBorderColor" to "#5a4f7c", // Node border
    //         "tertiaryColor" to "#40375c", // Container box background
    //         "lineColor" to "#f5a623",
    //         "fontSize" to "12px",
    //     ),
    //     focusColor = "#F5A622",
    //     moduleTypes = listOf(
    //         ModuleType.AndroidLibrary("#2C4162"),
    //     )
    // ),
    // )

    // You can add additional graphs.
    // A separate graph will be generated for each config below.
    // graph(
    //     readmePath = "./README.md",
    //     heading = "# Graph with root: gama",
    // ) {
    //     rootModulesRegex = ".*gama.*"
    // }
    // graph(
    //     readmePath = "./SomeOtherReadme.md",
    //     heading = "# Graph",
    // ) {
    //     rootModulesRegex = ".*zeta.*"
    // }
}