package dev.koga.deeplinklauncher.settings.ui.suggestions

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.launch

class SuggestionsOptionViewModel(
    private val preferencesDataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val enabled = !preferencesDataSource.preferences.shouldDisableDeepLinkSuggestions

    fun update(enabled: Boolean) {
        appCoroutineScope.launch {
            preferencesDataSource.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }
}
