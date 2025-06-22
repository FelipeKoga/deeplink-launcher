package dev.koga.deeplinklauncher.settings.impl.apptheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val dataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val appTheme = dataSource.preferencesStream
        .map { it.appTheme }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AppTheme.AUTO,
        )

    fun update(appTheme: AppTheme) {
        appCoroutineScope.launch {
            dataSource.updateTheme(appTheme)
        }
    }
}
