package dev.koga.deeplinklauncher.settings.ui.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import dev.koga.deeplinklauncher.purchase.Product
import dev.koga.deeplinklauncher.purchase.PurchaseApi
import dev.koga.deeplinklauncher.purchase.PurchaseResult
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val deepLinkDataSource: DeepLinkRepository,
    private val folderRepository: FolderRepository,
    private val preferencesRepository: PreferencesRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val purchaseApi: PurchaseApi,
) : ScreenModel {

    val preferences = preferencesRepository.preferencesStream.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = preferencesRepository.preferences,
    )

    val products = purchaseApi.getProducts().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = persistentListOf(),
    )

    private val messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = messageDispatcher.receiveAsFlow()

    val isPurchaseAvailable = purchaseApi.isAvailable

    fun changeTheme(theme: AppTheme) {
        screenModelScope.launch {
            preferencesRepository.updateTheme(theme)
        }
    }

    fun deleteAllDeepLinks() {
        deepLinkDataSource.deleteAll()
    }

    fun deleteAllFolders() {
        folderRepository.deleteAll()
    }

    fun deleteAllData() {
        deepLinkDataSource.deleteAll()
        folderRepository.deleteAll()
    }

    fun navigateToStore() {
        screenModelScope.launch {
            launchDeepLink.launch(ANDROID_PLAY_STORE_PATH)
        }
    }

    fun changeSuggestionsPreference(enabled: Boolean) {
        screenModelScope.launch {
            preferencesRepository.setShouldDisableDeepLinkSuggestions(!enabled)
        }
    }

    fun navigateToGithub() {
        screenModelScope.launch {
            launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
        }
    }

    fun purchaseProduct(product: Product) {
        screenModelScope.launch {
            when (val response = purchaseApi.purchase(product)) {
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
