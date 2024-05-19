package dev.koga.deeplinklauncher.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.model.Preferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences as DataStorePreferences

private val themeKey = stringPreferencesKey("theme")
private val shouldHideOnboardingKey = booleanPreferencesKey("should_hide_onboarding")

class PreferencesDataSource(
    private val dataStore: DataStore<DataStorePreferences>,
) {

    val preferencesStream = dataStore.data.map {
        Preferences(
            shouldHideOnboarding = it[shouldHideOnboardingKey] ?: false,
            appTheme = AppTheme.get(it[themeKey]),
        )
    }

    val preferences = runBlocking { preferencesStream.firstOrNull() ?: Preferences() }

    suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit {
            it[themeKey] = theme.name
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        dataStore.edit {
            it[shouldHideOnboardingKey] = shouldHideOnboarding
        }
    }
}
