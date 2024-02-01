package dev.koga.deeplinklauncher.di

import org.koin.core.module.Module
import org.koin.dsl.module

val importUiModule = module {
    includes(platformImportModule)
}

expect val platformImportModule: Module
