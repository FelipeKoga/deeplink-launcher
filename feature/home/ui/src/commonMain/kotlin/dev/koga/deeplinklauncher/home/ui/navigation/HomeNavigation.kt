package dev.koga.deeplinklauncher.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.home.ui.HomeScreen
import dev.koga.deeplinklauncher.home.ui.component.OnboardingBottomSheet
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.back
import org.koin.compose.viewmodel.koinViewModel

class HomeNavigation(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<AppNavigationRoute.Home> {
            HomeScreen(viewModel = koinViewModel())
        }

        dialog<AppNavigationRoute.Onboarding> {
            OnboardingBottomSheet(onDismiss = appNavigator::back)
        }
    }
}
