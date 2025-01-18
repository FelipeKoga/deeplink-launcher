package dev.koga.deeplinklauncher.home.ui.di

import dev.koga.deeplinklauncher.home.ui.screen.component.targets.DeeplinkTargetsDropdownManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformHomeUiModule: Module = module {
    factory { DeeplinkTargetsDropdownManager(get(), CoroutineScope(Dispatchers.IO)) }
}
