package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.deeplink.DuplicateDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::DuplicateDeepLink)
    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module
