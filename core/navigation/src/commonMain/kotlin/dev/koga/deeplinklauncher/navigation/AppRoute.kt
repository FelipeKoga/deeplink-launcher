package dev.koga.deeplinklauncher.navigation

import kotlinx.serialization.Serializable

public interface AppRoute {
    public val analyticsScreenName: String? get() = null

    @Serializable
    public data object PopBackStack : AppRoute
}
