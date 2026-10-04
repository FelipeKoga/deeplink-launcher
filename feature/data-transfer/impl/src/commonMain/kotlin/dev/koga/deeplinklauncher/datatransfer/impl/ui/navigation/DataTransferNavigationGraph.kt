package dev.koga.deeplinklauncher.datatransfer.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.koga.deeplinklauncher.datatransfer.api.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.metroViewModel

@ContributesIntoSet(AppScope::class)
class DataTransferNavigationGraph : NavigationGraph {

    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        composable<DataTransferRoute.ImportData> {
            ImportScreen(viewModel = metroViewModel())
        }

        composable<DataTransferRoute.ExportData> {
            ExportScreen(viewModel = metroViewModel())
        }
    }
}
