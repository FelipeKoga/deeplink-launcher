package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.HomeScreenModel
import dev.koga.deeplinklauncher.screen.component.launchtarget.DevicesDropdownManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val homeUiModule = module {
    factoryOf(::HomeScreenModel)
    factory { DevicesDropdownManager(get(), CoroutineScope(Dispatchers.IO)) }
}
