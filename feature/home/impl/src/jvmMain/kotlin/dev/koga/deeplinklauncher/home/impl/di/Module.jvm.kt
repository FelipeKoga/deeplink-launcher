package dev.koga.deeplinklauncher.home.impl.di

import dev.koga.deeplinklauncher.home.impl.ui.component.targets.DeepLinkTargetsDropdownViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformHomeUiModule: Module = module {
    viewModelOf(::DeepLinkTargetsDropdownViewModel)
}
