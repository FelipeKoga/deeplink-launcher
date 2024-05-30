package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.usecase.ImportDeepLinks
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::DuplicateDeepLink)
    singleOf(::ExportDeepLinks)
    singleOf(::GetDeepLinksJsonPreview)
    singleOf(::GetDeepLinksPlainTextPreview)
    singleOf(::GetDeepLinksAndFolderStream)
    singleOf(::GetAutoSuggestionLinks)
    singleOf(::ImportDeepLinks)

    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module
