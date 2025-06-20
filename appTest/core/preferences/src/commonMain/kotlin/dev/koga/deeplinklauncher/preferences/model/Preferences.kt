package dev.koga.deeplinklauncher.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val appTheme: AppTheme = AppTheme.AUTO,
    val shouldShowOnboarding: Boolean = false,
    val shouldDisableDeepLinkSuggestions: Boolean = false,
)
