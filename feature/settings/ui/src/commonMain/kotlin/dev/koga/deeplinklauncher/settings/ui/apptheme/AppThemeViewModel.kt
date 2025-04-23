package dev.koga.deeplinklauncher.settings.ui.apptheme

import androidx.lifecycle.ViewModel
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val dataSource: PreferencesDataSource,
    private val appCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val appTheme = dataSource.preferences.appTheme

    fun update(appTheme: AppTheme) {
        appCoroutineScope.launch {
            dataSource.updateTheme(appTheme)
        }
    }
}
