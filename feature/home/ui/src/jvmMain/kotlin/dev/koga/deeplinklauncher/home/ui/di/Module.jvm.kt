package dev.koga.deeplinklauncher.home.ui.di

import dev.koga.deeplinklauncher.home.ui.component.targets.DeepLinkTargetsDropdownManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformHomeUiModule: Module = module {
    factory { DeepLinkTargetsDropdownManager(get(), CoroutineScope(Dispatchers.IO)) }
}
