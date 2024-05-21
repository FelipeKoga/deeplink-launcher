package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.ImportDeepLinks
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val importDomainModule = module {
    singleOf(::ImportDeepLinks)
}
