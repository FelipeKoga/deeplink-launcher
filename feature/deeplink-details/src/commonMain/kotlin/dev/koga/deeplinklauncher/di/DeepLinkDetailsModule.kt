package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.DeepLinkDetailScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val deepLinkDetailsModule = module {
    factoryOf(::DeepLinkDetailScreenModel)
}
