package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val launchDeepLink: LaunchDeepLink,
) : ScreenModel {

    val preferences = preferencesDataSource.preferencesStream.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = preferencesDataSource.preferences,
    )

    fun changeTheme(theme: AppTheme) {
        screenModelScope.launch {
            preferencesDataSource.updateTheme(theme)
        }
    }

    fun deleteAllDeepLinks() {
        deepLinkDataSource.deleteAll()
    }

    fun deleteAllFolders() {
        folderDataSource.deleteAll()
    }

    fun deleteAllData() {
        deepLinkDataSource.deleteAll()
        folderDataSource.deleteAll()
    }

    fun navigateToStore() {
        launchDeepLink.launch(ANDROID_PLAY_STORE_PATH)
    }

    fun changeSuggestionsPreference(enabled: Boolean) {
        screenModelScope.launch {
            preferencesDataSource.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }

    fun navigateToGithub() {
        launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
    }

    companion object {
        private const val ANDROID_PLAY_STORE_PATH =
            "https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android"
    }
}
