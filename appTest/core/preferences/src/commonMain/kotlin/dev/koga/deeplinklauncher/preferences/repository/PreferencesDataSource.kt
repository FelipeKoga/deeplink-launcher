package dev.koga.deeplinklauncher.preferences.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.model.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences as StoragePreferences

interface PreferencesDataSource {
    val preferencesStream: Flow<Preferences>
    val preferences: Preferences

    suspend fun updateTheme(theme: AppTheme)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    suspend fun setShouldDisableDeepLinkSuggestions(shouldDisableDeepLinkSuggestions: Boolean)
}

internal class PreferencesDataStore(
    private val dataStore: DataStore<StoragePreferences>,
) : PreferencesDataSource {

    private val themeKey = stringPreferencesKey("theme")
    private val shouldShowOnboarding = booleanPreferencesKey("should_show_onboarding")
    private val shouldDisableDeepLinkSuggestions =
        booleanPreferencesKey("should_disable_deep_link_suggestions")

    override val preferencesStream = dataStore.data.map {
        Preferences(
            shouldShowOnboarding = it[shouldShowOnboarding] ?: true,
            appTheme = AppTheme.get(it[themeKey]),
            shouldDisableDeepLinkSuggestions = it[shouldDisableDeepLinkSuggestions] ?: false,
        )
    }

    override val preferences =
        runBlocking { preferencesStream.firstOrNull() ?: Preferences() }

    override suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit {
            it[themeKey] = theme.name
        }
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        dataStore.edit {
            it[shouldShowOnboarding] = !shouldHideOnboarding
        }
    }

    override suspend fun setShouldDisableDeepLinkSuggestions(
        shouldDisableDeepLinkSuggestions: Boolean,
    ) {
        dataStore.edit {
            it[this.shouldDisableDeepLinkSuggestions] = shouldDisableDeepLinkSuggestions
        }
    }
}
