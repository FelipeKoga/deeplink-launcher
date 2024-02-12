package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val exportDeepLinksDomainModule = module {
    factoryOf(::ExportDeepLinks)
    factoryOf(::GetDeepLinksJsonPreview)
    factoryOf(::GetDeepLinksPlainTextPreview)
}
