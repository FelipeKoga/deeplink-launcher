package dev.koga.deeplinklauncher.preferences.api.repository

import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    val preferencesStream: Flow<Preferences>

    val preferences: Preferences

    suspend fun updateTheme(theme: AppTheme)
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
    suspend fun setShouldDisableDeepLinkSuggestions(shouldDisableDeepLinkSuggestions: Boolean)
}
