package dev.koga.deeplinklauncher.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.purchase.api.Product
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.api.PurchaseResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val launchDeepLink: LaunchDeepLink,
    private val purchaseApi: PurchaseApi,
    private val appNavigator: AppNavigator,
) : ViewModel(), AppNavigator by appNavigator {

    private val messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = messageDispatcher.receiveAsFlow()

    val isPurchaseAvailable = purchaseApi.isAvailable

     fun navigateToStore() {
        viewModelScope.launch {
            launchDeepLink.launch(ANDROID_PLAY_STORE_PATH)
        }
    }

    fun navigateToGithub() {
        viewModelScope.launch {
            launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
        }
    }

    private fun purchaseProduct(product: Product) {
        viewModelScope.launch {
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
