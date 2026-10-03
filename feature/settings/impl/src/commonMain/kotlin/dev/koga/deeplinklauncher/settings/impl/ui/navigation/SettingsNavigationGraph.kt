package dev.koga.deeplinklauncher.settings.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.settings.api.ui.navigation.SettingsRouteEntryPoint
import dev.koga.deeplinklauncher.settings.impl.ui.SettingsScreen
import dev.koga.deeplinklauncher.settings.impl.ui.apptheme.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.deletedata.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.opensource.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.settings.impl.ui.products.ProductsBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.suggestions.SuggestionsOptionBottomSheet
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.metroViewModel

@ContributesIntoSet(AppScope::class)
internal class SettingsNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<SettingsRouteEntryPoint> {
            SettingsScreen(viewmodel = metroViewModel())
        }

        composable<SettingsRoute.OpenSourceLicenses> {
            OpenSourceLicensesScreen(
                onBack = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.AppThemeBottomSheet> {
            AppThemeBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.SuggestionsOptionBottomSheet> {
            SuggestionsOptionBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.DeleteDataBottomSheet> {
            DeleteDataBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.ProductsBottomSheet> {
            ProductsBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }
    }
}
