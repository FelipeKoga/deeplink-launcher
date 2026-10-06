package dev.koga.deeplinklauncher.datatransfer.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.koga.deeplinklauncher.datatransfer.api.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@ContributesIntoSet(AppScope::class)
class DataTransferNavigationGraph : NavigationGraph {

    override fun EntryProviderScope<NavKey>.entries() {
        entry<DataTransferRoute.ImportData> {
            ImportScreen(viewModel = metroViewModel())
        }

        entry<DataTransferRoute.ExportData> {
            ExportScreen(viewModel = metroViewModel())
        }
    }

    override fun PolymorphicModuleBuilder<NavKey>.routes() {
        subclass(DataTransferRoute.ImportData::class, DataTransferRoute.ImportData.serializer())
        subclass(DataTransferRoute.ExportData::class, DataTransferRoute.ExportData.serializer())
    }
}
