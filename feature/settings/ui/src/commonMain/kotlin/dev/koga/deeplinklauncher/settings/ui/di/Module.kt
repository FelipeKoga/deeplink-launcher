package dev.koga.deeplinklauncher.settings.ui.di

import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.settings.ui.SettingsViewModel
import dev.koga.deeplinklauncher.settings.ui.apptheme.AppThemeViewModel
import dev.koga.deeplinklauncher.settings.ui.deletedata.DeleteDataViewModel
import dev.koga.deeplinklauncher.settings.ui.navigation.SettingsNavigation
import dev.koga.deeplinklauncher.settings.ui.products.ProductsViewModel
import dev.koga.deeplinklauncher.settings.ui.suggestions.SuggestionsOptionViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsUiModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AppThemeViewModel)
    viewModelOf(::SuggestionsOptionViewModel)
    viewModelOf(::DeleteDataViewModel)
    viewModelOf(::ProductsViewModel)

    singleOf(::SettingsNavigation) bind NavigationGraph::class
}
