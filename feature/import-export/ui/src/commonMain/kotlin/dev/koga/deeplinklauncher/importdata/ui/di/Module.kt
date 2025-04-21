package dev.koga.deeplinklauncher.importdata.ui.di

import dev.koga.deeplinklauncher.importdata.ui.navigation.ImportExportNavigation
import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportViewModel
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportViewModel
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val importExportUiModule = module {
    viewModelOf(::ExportViewModel)
    viewModelOf(::ImportViewModel)
    singleOf(::ImportExportNavigation) bind NavigationGraph::class
}
