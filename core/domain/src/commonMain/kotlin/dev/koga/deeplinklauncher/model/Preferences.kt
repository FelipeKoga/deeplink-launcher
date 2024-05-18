package dev.koga.deeplinklauncher.model

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val appTheme: AppTheme,
    val shouldHideOnboarding: Boolean,
)
