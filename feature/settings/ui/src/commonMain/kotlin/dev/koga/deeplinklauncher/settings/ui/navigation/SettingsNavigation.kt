package dev.koga.deeplinklauncher.settings.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.settings.ui.SettingsScreen
import dev.koga.deeplinklauncher.settings.ui.apptheme.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.ui.deletedata.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.ui.opensource.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.settings.ui.products.ProductsBottomSheet
import dev.koga.deeplinklauncher.settings.ui.suggestions.SuggestionsOptionBottomSheet
import org.koin.compose.viewmodel.koinViewModel

class SettingsNavigation(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<AppNavigationRoute.Settings.Root> {
            SettingsScreen(viewmodel = koinViewModel())
        }

        composable<AppNavigationRoute.Settings.OpenSourceLicenses> {
            OpenSourceLicensesScreen(onBack = { appNavigator.navigate(AppNavigationRoute.Back) })
        }

        dialog<AppNavigationRoute.Settings.AppThemeBottomSheet> {
            AppThemeBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = { appNavigator.navigate(AppNavigationRoute.Back) },
            )
        }

        dialog<AppNavigationRoute.Settings.SuggestionsOptionBottomSheet> {
            SuggestionsOptionBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = { appNavigator.navigate(AppNavigationRoute.Back) },
            )
        }

        dialog<AppNavigationRoute.Settings.DeleteDataBottomSheet> {
            DeleteDataBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = { appNavigator.navigate(AppNavigationRoute.Back) },
            )
        }

        dialog<AppNavigationRoute.Settings.Products> {
            ProductsBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = { appNavigator.navigate(AppNavigationRoute.Back) },
            )
        }
    }
}
