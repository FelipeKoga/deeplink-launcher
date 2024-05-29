package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val exportDeepLinksDomainModule = module {
    singleOf(::ExportDeepLinks)
    singleOf(::GetDeepLinksJsonPreview)
    singleOf(::GetDeepLinksPlainTextPreview)
}

