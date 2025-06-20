package dev.koga.deeplinklauncher.importdata.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.compose.viewmodel.koinViewModel

class ImportExportNavigation : NavigationGraph {

    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<AppNavigationRoute.ImportData> {
            ImportScreen(viewModel = koinViewModel())
        }

        composable<AppNavigationRoute.ExportData> {
            ExportScreen(viewModel = koinViewModel())
        }
    }
}
