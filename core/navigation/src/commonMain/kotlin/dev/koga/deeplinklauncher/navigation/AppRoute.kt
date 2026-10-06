package dev.koga.deeplinklauncher.navigation

import androidx.navigation3.runtime.NavKey

public interface AppRoute : NavKey {
    public val analyticsScreenName: String? get() = null
}
