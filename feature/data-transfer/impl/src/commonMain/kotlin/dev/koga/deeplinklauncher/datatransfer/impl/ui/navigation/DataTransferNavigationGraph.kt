package dev.koga.deeplinklauncher.datatransfer.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.datatransfer.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.compose.viewmodel.koinViewModel

class DataTransferNavigationGraph : NavigationGraph {

    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<DataTransferRoute.ImportData> {
            ImportScreen(viewModel = koinViewModel())
        }

        composable<DataTransferRoute.ExportData> {
            ExportScreen(viewModel = koinViewModel())
        }
    }
}
