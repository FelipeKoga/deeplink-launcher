package dev.koga.deeplinklauncher.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences as DataStorePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.koga.deeplinklauncher.model.Preferences
import dev.koga.deeplinklauncher.model.SystemTheme
import kotlinx.coroutines.flow.map


private val themeKey = stringPreferencesKey("theme")
private val shouldHideOnboardingKey = booleanPreferencesKey("should_hide_onboarding")

class PreferencesDataSource(
    private val dataStore: DataStore<DataStorePreferences>
) {

    val preferences = dataStore.data.map {
        Preferences(
            shouldHideOnboarding = it[shouldHideOnboardingKey] ?: false,
            systemTheme = SystemTheme.get(it[themeKey])
        )
    }

    suspend fun updateTheme(theme: SystemTheme) {
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