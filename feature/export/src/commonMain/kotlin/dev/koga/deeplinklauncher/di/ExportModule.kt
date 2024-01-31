package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.ExportScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val exportModule = module {
    factoryOf(::ExportScreenModel)
}
