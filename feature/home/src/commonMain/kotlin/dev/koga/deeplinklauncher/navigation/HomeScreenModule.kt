package dev.koga.deeplinklauncher.navigation

import dev.koga.deeplinklauncher.HomeScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeScreenModule = module {
    factoryOf(::HomeScreenModel)
}
