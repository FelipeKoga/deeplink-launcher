package dev.koga.deeplinklauncher.home.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.home.impl.ui.HomeScreen
import dev.koga.deeplinklauncher.home.impl.ui.onboarding.OnboardingBottomSheet
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.launch

@ContributesIntoSet(AppScope::class)
class HomeNavigationGraph(
    private val appNavigator: AppNavigator,
    private val appCoroutineScope: AppCoroutineScope,
    private val preferencesDataSource: PreferencesDataSource,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<HomeRoute.Home> {
            HomeScreen(viewModel = metroViewModel(), appNavigator = appNavigator)
        }

        dialog<HomeRoute.Onboarding> {
            OnboardingBottomSheet(onDismiss = {
                appCoroutineScope.launch {
                    preferencesDataSource.setShouldHideOnboarding(true)
                }
                appNavigator.popBackStack()
            })
        }
    }
}
