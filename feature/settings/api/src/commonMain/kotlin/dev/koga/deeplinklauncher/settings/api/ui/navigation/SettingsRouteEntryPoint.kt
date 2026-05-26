package dev.koga.deeplinklauncher.settings.api.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public data object SettingsRouteEntryPoint : AppRoute {
    @Transient
    override val analyticsScreenName: String = "settings"
}
