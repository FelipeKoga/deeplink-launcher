package dev.koga.deeplinklauncher.settings.impl.di

import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.settings.impl.SettingsViewModel
import dev.koga.deeplinklauncher.settings.impl.apptheme.AppThemeViewModel
import dev.koga.deeplinklauncher.settings.impl.deletedata.DeleteDataViewModel
import dev.koga.deeplinklauncher.settings.impl.navigation.SettingsNavigationGraph
import dev.koga.deeplinklauncher.settings.impl.products.ProductsViewModel
import dev.koga.deeplinklauncher.settings.impl.suggestions.SuggestionsOptionViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AppThemeViewModel)
    viewModelOf(::SuggestionsOptionViewModel)
    viewModelOf(::DeleteDataViewModel)
    viewModelOf(::ProductsViewModel)

    singleOf(::SettingsNavigationGraph) bind NavigationGraph::class
}
