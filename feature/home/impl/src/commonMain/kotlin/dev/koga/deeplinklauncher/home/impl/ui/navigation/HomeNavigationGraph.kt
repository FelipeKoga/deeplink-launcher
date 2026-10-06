package dev.koga.deeplinklauncher.home.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
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
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@ContributesIntoSet(AppScope::class)
class HomeNavigationGraph(
    private val appNavigator: AppNavigator,
    private val appCoroutineScope: AppCoroutineScope,
    private val preferencesDataSource: PreferencesDataSource,
) : NavigationGraph {
    override fun EntryProviderScope<NavKey>.entries() {
        entry<HomeRoute.Home> {
            HomeScreen(viewModel = metroViewModel(), appNavigator = appNavigator)
        }

        entry<HomeRoute.Onboarding>(metadata = DialogSceneStrategy.dialog()) {
            OnboardingBottomSheet(onDismiss = {
                appCoroutineScope.launch {
                    preferencesDataSource.setShouldHideOnboarding(true)
                }
                appNavigator.popBackStack()
            })
        }
    }

    override fun PolymorphicModuleBuilder<NavKey>.routes() {
        subclass(HomeRoute.Home::class, HomeRoute.Home.serializer())
        subclass(HomeRoute.Onboarding::class, HomeRoute.Onboarding.serializer())
    }
}
