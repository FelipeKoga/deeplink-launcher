package dev.koga.deeplinklauncher

import Product
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.purchase.PurchaseApi
import dev.koga.deeplinklauncher.purchase.PurchaseResult
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val purchase: PurchaseApi,
) : ScreenModel {

    val preferences = preferencesDataSource.preferencesStream.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = preferencesDataSource.preferences,
    )

    val products = purchase.getProducts().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = persistentListOf()
    )

    private val messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = messageDispatcher.receiveAsFlow()

    val isPurchaseAvailable = purchase.isAvailable

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
        screenModelScope.launch {
            launchDeepLink.launch(ANDROID_PLAY_STORE_PATH)
        }
    }

    fun changeSuggestionsPreference(enabled: Boolean) {
        screenModelScope.launch {
            preferencesDataSource.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }

    fun navigateToGithub() {
        screenModelScope.launch {
            launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
        }
    }

    fun purchaseProduct(product: Product) {
        screenModelScope.launch {
            when (val response = purchase.purchase(product)) {
                PurchaseResult.Success -> {
                    messageDispatcher.send("Thank you for your support!")
                }

                is PurchaseResult.Error -> {
                    if (!response.userCancelled) {
                        messageDispatcher.send("Something went wrong, please try again")
                    }
                }
            }
        }
    }

    companion object {
        private const val ANDROID_PLAY_STORE_PATH =
            "https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android"
    }
}
