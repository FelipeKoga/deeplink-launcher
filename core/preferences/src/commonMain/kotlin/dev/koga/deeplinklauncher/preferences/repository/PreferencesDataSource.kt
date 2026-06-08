package dev.koga.deeplinklauncher.preferences.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.model.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.datastore.preferences.core.Preferences as StoragePreferences

interface PreferencesDataSource {
    val preferencesStream: Flow<Preferences>
    val preferences: Preferences?

    suspend fun updateTheme(theme: AppTheme)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    suspend fun setShouldDisableDeepLinkSuggestions(shouldDisableDeepLinkSuggestions: Boolean)
}

internal class PreferencesDataStore(
    private val dataStore: DataStore<StoragePreferences>,
) : PreferencesDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val themeKey = stringPreferencesKey("theme")
    private val shouldShowOnboarding = booleanPreferencesKey("should_show_onboarding")
    private val shouldDisableDeepLinkSuggestions =
        booleanPreferencesKey("should_disable_deep_link_suggestions")

    private val preferencesState = dataStore.data.map {
        Preferences(
            shouldShowOnboarding = it[shouldShowOnboarding] ?: true,
            appTheme = AppTheme.get(it[themeKey]),
            shouldDisableDeepLinkSuggestions = it[shouldDisableDeepLinkSuggestions] ?: false,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    override val preferencesStream: Flow<Preferences> = preferencesState.filterNotNull()

    override val preferences: Preferences?
        get() = preferencesState.value

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
