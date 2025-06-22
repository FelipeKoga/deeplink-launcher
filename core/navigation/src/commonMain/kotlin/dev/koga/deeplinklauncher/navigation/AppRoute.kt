package dev.koga.deeplinklauncher.navigation

import kotlinx.serialization.Serializable

public interface AppRoute {
    @Serializable
    public data object PopBackStack : AppRoute
}
