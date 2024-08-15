package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.HomeScreenModel
import dev.koga.deeplinklauncher.screen.component.launchtarget.LaunchTargetManager
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val homeUiModule = module {
    factoryOf(::HomeScreenModel)
    factoryOf(::LaunchTargetManager)
}