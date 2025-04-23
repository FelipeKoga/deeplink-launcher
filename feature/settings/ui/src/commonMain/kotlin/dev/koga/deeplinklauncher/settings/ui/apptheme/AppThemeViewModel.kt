package dev.koga.deeplinklauncher.settings.ui.apptheme

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val repository: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val appTheme = repository.preferences.appTheme

    fun update(appTheme: AppTheme) {
        appCoroutineScope.launch {
            repository.updateTheme(appTheme)
        }
    }
}
