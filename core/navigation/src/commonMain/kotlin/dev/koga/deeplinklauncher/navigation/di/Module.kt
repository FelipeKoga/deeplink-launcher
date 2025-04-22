package dev.koga.deeplinklauncher.navigation.di

import dev.koga.deeplinklauncher.navigation.AppGraph
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppNavigatorImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val navigationModule: Module = module {
    singleOf(::AppNavigatorImpl) bind AppNavigator::class
    single { AppGraph(getAll()) }
}
