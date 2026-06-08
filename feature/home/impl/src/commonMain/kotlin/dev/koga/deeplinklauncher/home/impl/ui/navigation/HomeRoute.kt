package dev.koga.deeplinklauncher.home.impl.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

sealed interface HomeRoute : AppRoute {

    @Serializable
    data object Home : HomeRoute {
        @Transient
        override val analyticsScreenName: String = "home"
    }

    @Serializable
    data object Onboarding : HomeRoute {
        @Transient
        override val analyticsScreenName: String = "onboarding"
    }
}
