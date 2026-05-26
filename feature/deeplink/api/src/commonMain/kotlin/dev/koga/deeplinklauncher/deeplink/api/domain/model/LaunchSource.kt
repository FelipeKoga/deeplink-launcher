package dev.koga.deeplinklauncher.deeplink.api.domain.model

public enum class LaunchSource(public val value: String) {
    INPUT_BAR("input_bar"),
    LIST("list"),
    DETAILS("details"),
    FOLDER("folder"),
    LINK_FLOW("link_flow"),
}
