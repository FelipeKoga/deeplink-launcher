package dev.koga.deeplinklauncher.settings.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.popBackStack
import dev.koga.deeplinklauncher.settings.api.SettingsEntryPoint
import dev.koga.deeplinklauncher.settings.impl.SettingsScreen
import dev.koga.deeplinklauncher.settings.impl.apptheme.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.impl.deletedata.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.impl.opensource.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.settings.impl.products.ProductsBottomSheet
import dev.koga.deeplinklauncher.settings.impl.suggestions.SuggestionsOptionBottomSheet
import org.koin.compose.viewmodel.koinViewModel

internal class SettingsNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<SettingsEntryPoint> {
            SettingsScreen(viewmodel = koinViewModel())
        }

        composable<SettingsRoute.OpenSourceLicenses> {
            OpenSourceLicensesScreen(
                onBack = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.AppThemeBottomSheet> {
            AppThemeBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.SuggestionsOptionBottomSheet> {
            SuggestionsOptionBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.DeleteDataBottomSheet> {
            DeleteDataBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        dialog<SettingsRoute.ProductsBottomSheet> {
            ProductsBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }
    }
}
