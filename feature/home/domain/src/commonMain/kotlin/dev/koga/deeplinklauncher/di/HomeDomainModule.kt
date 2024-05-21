package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeDomainModule = module {
    singleOf(::GetDeepLinksAndFolderStream)
    singleOf(::GetAutoSuggestionLinks)
    includes(platformHomeDomainModule)
}

expect val platformHomeDomainModule: Module
