package dev.koga.deeplinklauncher.datasource

import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {

    val preferencesStream: Flow<Preferences>

    val preferences: Preferences

    suspend fun updateTheme(theme: AppTheme)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    suspend fun setShouldDisableDeepLinkSuggestions(shouldDisableDeepLinkSuggestions: Boolean)
}
