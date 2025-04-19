package dev.koga.deeplinklauncher.settings.ui

import dev.koga.deeplinklauncher.navigation.Route
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.purchase.api.Product

sealed interface SettingsAction {
    data class UpdateSuggestionsPreferences(val enabled: Boolean) : SettingsAction
    data class Purchase(val product: Product) : SettingsAction
    data class Navigate(val route: Route) : SettingsAction
    data class UpdateAppTheme(val appTheme: AppTheme) : SettingsAction
    data object NavigateToGithub : SettingsAction
    data object NavigateToStore : SettingsAction
}