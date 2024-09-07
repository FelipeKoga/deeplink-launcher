package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.component.launchtarget.DevicesDropdownManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformHomeUiModule: Module = module {
    factory { DevicesDropdownManager(get(), CoroutineScope(Dispatchers.IO)) }
}
