package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.platform.Platform

sealed class Target {

    data object Browser : Target()

    sealed class Device : Target() {

        abstract val platform: Platform
        abstract val active: Boolean
        abstract val name: String
        abstract val serial: String

        data class Emulator(
            override val serial: String,
            override val platform: Platform,
            override val active: Boolean = true,
            override val name: String = serial,
        ) : Device()

        data class Physical(
            override val serial: String,
            override val platform: Platform,
            override val active: Boolean = true,
            override val name: String = serial,
        ) : Device()
    }
}
