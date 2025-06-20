package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavGraphBuilder

public interface NavigationGraph {
    public fun register(navGraphBuilder: NavGraphBuilder)
}
