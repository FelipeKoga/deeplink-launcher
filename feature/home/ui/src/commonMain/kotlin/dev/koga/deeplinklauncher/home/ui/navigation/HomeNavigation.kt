package dev.koga.deeplinklauncher.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.home.ui.HomeScreen
import dev.koga.deeplinklauncher.home.ui.component.OnboardingBottomSheet
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.back
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class HomeNavigation(
    private val appNavigator: AppNavigator,
    private val appCoroutineScope: AppCoroutineScope,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<AppNavigationRoute.Home> {
            HomeScreen(viewModel = koinViewModel(), appNavigator = appNavigator)
        }

        dialog<AppNavigationRoute.Onboarding> {
            val preferencesRepository = koinInject<PreferencesRepository>()
            OnboardingBottomSheet(onDismiss = {
                appCoroutineScope.launch {
                    preferencesRepository.setShouldHideOnboarding(true)
                }
                appNavigator.back()
            })
        }
    }
}
