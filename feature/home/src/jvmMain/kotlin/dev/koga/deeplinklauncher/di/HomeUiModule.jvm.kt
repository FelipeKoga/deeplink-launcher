package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.HomeScreenModel
import dev.koga.deeplinklauncher.screen.component.DevicesDataSource
import dev.koga.deeplinklauncher.screen.component.LaunchTargetManager
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val homeUiModule = module {
    factoryOf(::HomeScreenModel)
    factoryOf(::DevicesDataSource)
    factoryOf(::LaunchTargetManager)
}