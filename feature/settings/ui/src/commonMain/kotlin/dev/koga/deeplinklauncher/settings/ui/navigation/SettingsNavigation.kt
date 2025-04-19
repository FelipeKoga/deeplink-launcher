package dev.koga.deeplinklauncher.settings.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.navigation.FeatureNavigationApi
import dev.koga.deeplinklauncher.navigation.Route
import dev.koga.deeplinklauncher.settings.ui.SettingsScreen
import dev.koga.deeplinklauncher.settings.ui.apptheme.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.ui.deletedata.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.ui.opensource.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.settings.ui.suggestions.SuggestionsOptionBottomSheet
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

sealed interface SettingsRoute : Route {
    @Serializable
    data object Settings : SettingsRoute

    @Serializable
    data object OpenSourceLicenses : SettingsRoute

    @Serializable
    data object AppThemeBottomSheet : SettingsRoute

    @Serializable
    data object SuggestionsOptionBottomSheet : SettingsRoute

    @Serializable
    data object DeleteDataBottomSheet : SettingsRoute
}


class SettingsNavigation : FeatureNavigationApi {

    override val rootRoute: Route = SettingsRoute.Settings

    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composable<SettingsRoute.Settings> {
            SettingsScreen(
                viewmodel = koinViewModel(),
                navHostController = navHostController
            )
        }

        composable<SettingsRoute.OpenSourceLicenses> {
            OpenSourceLicensesScreen(onBack = navHostController::popBackStack)
        }

        dialog<SettingsRoute.AppThemeBottomSheet> {
            AppThemeBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = navHostController::popBackStack
            )
        }

        dialog<SettingsRoute.SuggestionsOptionBottomSheet> {
            SuggestionsOptionBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = navHostController::popBackStack
            )
        }

        dialog<SettingsRoute.DeleteDataBottomSheet> {
            DeleteDataBottomSheet(
                viewModel = koinViewModel(),
                onDismissRequest = navHostController::popBackStack
            )
        }
    }
}