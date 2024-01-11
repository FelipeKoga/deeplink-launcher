package com.point.android.feature.home.di

import dev.koga.deeplinklauncher.android.home.HomeScreenModel
import org.koin.dsl.module

val homeModule = module {
    factory { HomeScreenModel(get(), get()) }
}