package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.ExportScreenModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val exportDeepLinksUiModule = module {
    factoryOf(::ExportScreenModel)

    includes(platformUiModule)
}

expect val platformUiModule: Module
