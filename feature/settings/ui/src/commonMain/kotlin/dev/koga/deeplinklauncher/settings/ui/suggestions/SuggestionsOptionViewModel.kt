package dev.koga.deeplinklauncher.settings.ui.suggestions

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.repository.PreferencesRepository
import kotlinx.coroutines.launch

class SuggestionsOptionViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val enabled = !preferencesRepository.preferences.shouldDisableDeepLinkSuggestions

    fun update(enabled: Boolean) {
        appCoroutineScope.launch {
            preferencesRepository.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }
}
