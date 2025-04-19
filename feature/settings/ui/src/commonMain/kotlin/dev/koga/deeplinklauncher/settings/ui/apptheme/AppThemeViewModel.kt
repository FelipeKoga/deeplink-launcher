package dev.koga.deeplinklauncher.settings.ui.apptheme

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val repository: PreferencesRepository,
    private val appCoroutineScope: AppCoroutineScope
) : ViewModel() {

    val appTheme = repository.preferences.appTheme

    fun update(appTheme: AppTheme) {
        appCoroutineScope.launch {
            repository.updateTheme(appTheme)
        }
    }
}