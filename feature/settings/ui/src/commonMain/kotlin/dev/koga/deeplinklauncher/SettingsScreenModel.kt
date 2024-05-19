package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.platform.PlatformInfo
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val platformInfo: PlatformInfo,
) : ScreenModel {

    val appVersion = platformInfo.version
    val appTheme = preferencesDataSource.preferencesStream.map { it.appTheme }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AppTheme.AUTO,
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
        launchDeepLink.launch(platformInfo.storePath)
    }

    fun navigateToGithub() {
        launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
    }
}
