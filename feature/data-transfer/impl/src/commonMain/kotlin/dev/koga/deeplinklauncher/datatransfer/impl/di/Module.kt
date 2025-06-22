package dev.koga.deeplinklauncher.datatransfer.impl.di

import dev.koga.deeplinklauncher.datatransfer.domain.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.domain.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.datatransfer.domain.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.datatransfer.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase.ExportDeepLinksImpl
import dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase.GetDeepLinksJsonPreviewImpl
import dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase.GetDeepLinksPlainTextPreviewImpl
import dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase.ImportDeepLinksImpl
import dev.koga.deeplinklauncher.datatransfer.impl.ui.navigation.DataTransferNavigationGraph
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export.ExportViewModel
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.ImportViewModel
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataTransferModule = module {
    singleOf(::ExportDeepLinksImpl) bind ExportDeepLinks::class
    singleOf(::ImportDeepLinksImpl) bind ImportDeepLinks::class
    singleOf(::GetDeepLinksJsonPreviewImpl) bind GetDeepLinksJsonPreview::class
    singleOf(::GetDeepLinksPlainTextPreviewImpl) bind GetDeepLinksPlainTextPreview::class
    singleOf(::ImportDeepLinksImpl) bind ImportDeepLinks::class
    singleOf(::DataTransferNavigationGraph) bind NavigationGraph::class

    viewModelOf(::ImportViewModel)
    viewModelOf(::ExportViewModel)
}
