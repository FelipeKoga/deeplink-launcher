package dev.koga.deeplinklauncher.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.koga.deeplinklauncher.home.ui.HomeScreen
import dev.koga.deeplinklauncher.navigation.Route
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeNavGraph() {
    composable<Route.Home> {
        HomeScreen(viewModel = koinViewModel())
    }
}