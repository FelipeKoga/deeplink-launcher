package dev.koga.deeplinklauncher.di

import org.koin.core.module.Module
import org.koin.dsl.module

val importModule = module {
    includes(platformImportModule)
}

expect val platformImportModule: Module