package dev.koga.deeplinklauncher.importdata.ui.di

import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportViewModel
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val importExportUiModule = module {
    viewModelOf(::ExportViewModel)
    viewModelOf(::ImportViewModel)
}
