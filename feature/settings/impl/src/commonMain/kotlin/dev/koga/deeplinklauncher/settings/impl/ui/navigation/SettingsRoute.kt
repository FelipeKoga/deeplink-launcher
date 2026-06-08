package dev.koga.deeplinklauncher.settings.impl.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

sealed interface SettingsRoute : AppRoute {
    @Serializable
    data object OpenSourceLicenses : SettingsRoute {
        @Transient
        override val analyticsScreenName: String = "open_source_licenses"
    }

    @Serializable
    data object AppThemeBottomSheet : SettingsRoute {
        @Transient
        override val analyticsScreenName: String = "app_theme"
    }

    @Serializable
    data object SuggestionsOptionBottomSheet : SettingsRoute {
        @Transient
        override val analyticsScreenName: String = "suggestions_option"
    }

    @Serializable
    data object DeleteDataBottomSheet : SettingsRoute {
        @Transient
        override val analyticsScreenName: String = "delete_data"
    }

    @Serializable
    data object ProductsBottomSheet : SettingsRoute {
        @Transient
        override val analyticsScreenName: String = "products"
    }
}
