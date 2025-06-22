package dev.koga.deeplinklauncher.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.home.HomeScreen
import dev.koga.deeplinklauncher.home.onboarding.OnboardingBottomSheet
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.popBackStack
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class HomeNavigationGraph(
    private val appNavigator: AppNavigator,
    private val appCoroutineScope: AppCoroutineScope,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<HomeRoute.Home> {
            HomeScreen(viewModel = koinViewModel(), appNavigator = appNavigator)
        }

        dialog<HomeRoute.Onboarding> {
            val preferencesDataSource = koinInject<PreferencesDataSource>()
            OnboardingBottomSheet(onDismiss = {
                appCoroutineScope.launch {
                    preferencesDataSource.setShouldHideOnboarding(true)
                }
                appNavigator.popBackStack()
            })
        }
    }
}
