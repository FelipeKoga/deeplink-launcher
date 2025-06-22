package dev.koga.deeplinklauncher.settings.impl.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

internal sealed interface SettingsRoute : AppRoute {
    @Serializable
    data object OpenSourceLicenses : SettingsRoute

    @Serializable
    data object AppThemeBottomSheet : SettingsRoute

    @Serializable
    data object SuggestionsOptionBottomSheet : SettingsRoute

    @Serializable
    data object DeleteDataBottomSheet : SettingsRoute

    @Serializable
    data object ProductsBottomSheet : SettingsRoute
}
