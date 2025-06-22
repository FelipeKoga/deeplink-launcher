package dev.koga.deeplinklauncher.settings.impl.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SuggestionsOptionViewModel(
    private val preferencesDataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val enabled = preferencesDataSource.preferencesStream
        .map { !it.shouldDisableDeepLinkSuggestions }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    fun update(enabled: Boolean) {
        appCoroutineScope.launch {
            preferencesDataSource.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }
}
