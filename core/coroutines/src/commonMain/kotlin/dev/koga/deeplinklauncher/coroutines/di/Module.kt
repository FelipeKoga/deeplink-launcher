package dev.koga.deeplinklauncher.navigation.di

import dev.koga.deeplinklauncher.DLLNavigator
import dev.koga.deeplinklauncher.DLLNavigatorImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val navigationModule = module {
    single<DLLNavigator> { DLLNavigatorImpl(Dispatchers.Main.immediate) }
}