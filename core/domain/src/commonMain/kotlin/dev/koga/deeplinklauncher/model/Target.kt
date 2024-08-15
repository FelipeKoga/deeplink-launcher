package dev.koga.deeplinklauncher.model

sealed class Target {

    abstract val name: String

    data object Browser : Target() {
        override val name = "browser"
    }

    data class Device(
        override val name: String,
        val type: String
    ) : Target() {
        val isActive = type == "device"
    }
}
