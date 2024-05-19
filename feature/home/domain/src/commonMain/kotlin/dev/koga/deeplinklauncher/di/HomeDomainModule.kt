package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeDomainModule = module {
    factoryOf(::GetDeepLinksAndFolderStream)
    factoryOf(::GetAutoSuggestionLinks)
    includes(platformHomeDomainModule)
}

expect val platformHomeDomainModule: Module
