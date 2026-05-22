package dev.koga.deeplinklauncher.home.impl.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

sealed interface HomeRoute : AppRoute {

    @Serializable
    data object Home : HomeRoute

    @Serializable
    data object Onboarding : HomeRoute
}
