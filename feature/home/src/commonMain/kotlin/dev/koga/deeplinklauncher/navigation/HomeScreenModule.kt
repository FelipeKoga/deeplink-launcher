package dev.koga.deeplinklauncher.navigation

import dev.koga.deeplinklauncher.HomeScreenModel
import org.koin.dsl.module

val homeScreenModule = module {
    factory { HomeScreenModel(get(), get(), get(), get(), get(), get()) }
}
