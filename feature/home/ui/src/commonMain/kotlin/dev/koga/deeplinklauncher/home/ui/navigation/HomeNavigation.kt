package dev.koga.deeplinklauncher.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.koga.deeplinklauncher.home.ui.HomeScreen
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.compose.viewmodel.koinViewModel

class HomeNavigation : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<AppNavigationRoute.Home> {
            HomeScreen(viewModel = koinViewModel())
        }
    }
}
