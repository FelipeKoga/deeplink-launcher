package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.usecase.DuplicateDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::DuplicateDeepLink)
    singleOf(::ExportDeepLinks)
    singleOf(::GetDeepLinksJsonPreview)
    singleOf(::GetDeepLinksPlainTextPreview)

    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module
