package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.datastore.dataStore
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object PreferencesBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun preferencesDataSource(): PreferencesDataSource = PreferencesDataStore(dataStore())
}
