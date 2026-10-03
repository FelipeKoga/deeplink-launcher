package dev.koga.deeplinklauncher.settings.impl.ui.apptheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.settings.impl.analytics.ThemeChanged
import dev.koga.deeplinklauncher.settings.impl.analytics.track
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ViewModelKey
@ContributesIntoMap(AppScope::class)
class AppThemeViewModel(
    private val dataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    val appTheme = dataSource.preferencesStream
        .map { it.appTheme }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppTheme.AUTO,
        )

    fun update(appTheme: AppTheme) {
        analyticsTracker.track(ThemeChanged(theme = appTheme.name.lowercase()))
        appCoroutineScope.launch {
            dataSource.updateTheme(appTheme)
        }
    }
}
