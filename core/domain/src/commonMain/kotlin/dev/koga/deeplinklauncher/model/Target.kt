package dev.koga.deeplinklauncher.model

sealed class Target {

    data object Browser : Target()

    sealed class Device : Target() {

        abstract val active: Boolean
        abstract val name: String
        abstract val serial: String

        data class Emulator(
            override val serial: String,
            override val active: Boolean = true,
            override val name: String = serial
        ) : Device()

        data class Physical(
            override val serial: String,
            override val active: Boolean = true,
            override val name: String = serial
        ) : Device()
    }
}
