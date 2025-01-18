package dev.koga.deeplinklauncher.importdata.ui.di

import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportScreenModel
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val importExportUiModule = module {
    factoryOf(::ExportScreenModel)
    factoryOf(::ImportScreenModel)
}
