package dev.koga.deeplinklauncher.importexport.impl.di

import dev.koga.deeplinklauncher.importexport.impl.usecase.DefaultExportDeepLinks
import dev.koga.deeplinklauncher.importexport.impl.usecase.DefaultGetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.importexport.impl.usecase.DefaultImportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val importExportImplModule = module {
    singleOf(::DefaultExportDeepLinks) bind ExportDeepLinks::class
    singleOf(::DefaultImportDeepLinks) bind ImportDeepLinks::class
    singleOf(::DefaultGetDeepLinksJsonPreview) bind GetDeepLinksJsonPreview::class
    singleOf(::DefaultImportDeepLinks) bind ImportDeepLinks::class
}
