package dev.koga.deeplinklauncher.settings.impl.ui.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.settings.impl.analytics.SuggestionsToggled
import dev.koga.deeplinklauncher.settings.impl.analytics.track
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SuggestionsOptionViewModel(
    private val preferencesDataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    val enabled = preferencesDataSource.preferencesStream
        .map { !it.shouldDisableDeepLinkSuggestions }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    fun update(enabled: Boolean) {
        analyticsTracker.track(SuggestionsToggled(enabled = enabled))
        appCoroutineScope.launch {
            preferencesDataSource.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }
}
