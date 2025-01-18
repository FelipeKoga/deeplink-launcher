package dev.koga.deeplinklauncher.file.di

import org.koin.core.module.Module
import org.koin.dsl.module

val fileModule = module {
    includes(platformModule)
}

internal expect val platformModule: Module
