package dev.koga.deeplinklauncher.model

sealed class Target {

    abstract val name: String

    data object Browser : Target() {
        override val name = "browser"
    }

    sealed class Device : Target() {

        abstract val active: Boolean

        data class Emulator(
            override val name: String,
            override val active: Boolean
        ) : Device()

        data class Physical(
            override val name: String,
            override val active: Boolean
        ) : Device()
    }
}
