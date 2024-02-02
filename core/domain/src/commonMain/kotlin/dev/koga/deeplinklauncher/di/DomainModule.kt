package dev.koga.deeplinklauncher.di

import org.koin.core.module.Module
import org.koin.dsl.module

val domainModule = module {
    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module
