package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.deeplink.DuplicateDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::DuplicateDeepLink)
    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module
