package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.ImportDeepLinks
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val importDomainModule = module {
    factoryOf(::ImportDeepLinks)
}
