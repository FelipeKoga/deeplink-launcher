package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.details.DeepLinkDetailsScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val deepLinkDetailsUiModule = module {
    factoryOf(::DeepLinkDetailsScreenModel)
}
