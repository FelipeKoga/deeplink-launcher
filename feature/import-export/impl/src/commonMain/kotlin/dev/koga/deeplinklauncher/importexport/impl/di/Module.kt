package dev.koga.deeplinklauncher.importexport.impl.di

import dev.koga.deeplinklauncher.importexport.impl.usecase.ExportDeepLinksImpl
import dev.koga.deeplinklauncher.importexport.impl.usecase.GetDeepLinksJsonPreviewImpl
import dev.koga.deeplinklauncher.importexport.impl.usecase.GetDeepLinksPlainTextPreviewImpl
import dev.koga.deeplinklauncher.importexport.impl.usecase.ImportDeepLinksImpl
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val importExportImplModule = module {
    singleOf(::ExportDeepLinksImpl) bind ExportDeepLinks::class
    singleOf(::ImportDeepLinksImpl) bind ImportDeepLinks::class
    singleOf(::GetDeepLinksJsonPreviewImpl) bind GetDeepLinksJsonPreview::class
    singleOf(::GetDeepLinksPlainTextPreviewImpl) bind GetDeepLinksPlainTextPreview::class
    singleOf(::ImportDeepLinksImpl) bind ImportDeepLinks::class
}
