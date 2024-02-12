package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeDomainModule = module {
    factoryOf(::GetDeepLinksAndFolderStream)
}
