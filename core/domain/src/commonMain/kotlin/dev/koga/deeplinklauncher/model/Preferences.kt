package dev.koga.deeplinklauncher.model

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val systemTheme: SystemTheme,
    val shouldHideOnboarding: Boolean,
)
