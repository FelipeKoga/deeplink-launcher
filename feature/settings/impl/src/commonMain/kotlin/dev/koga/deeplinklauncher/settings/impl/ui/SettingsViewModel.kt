package dev.koga.deeplinklauncher.settings.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.settings.impl.analytics.ExternalLinkOpened
import dev.koga.deeplinklauncher.settings.impl.analytics.track
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val launchDeepLink: LaunchDeepLink,
    private val purchaseApi: PurchaseApi,
    private val appNavigator: AppNavigator,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel(), AppNavigator by appNavigator {

    private val messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = messageDispatcher.receiveAsFlow()

    val isPurchaseAvailable = purchaseApi.isAvailable

    fun navigateToStore() {
        analyticsTracker.track(ExternalLinkOpened(destination = "play_store"))
        viewModelScope.launch {
            launchDeepLink.launch(ANDROID_PLAY_STORE_PATH)
        }
    }

    fun navigateToGithub() {
        analyticsTracker.track(ExternalLinkOpened(destination = "github"))
        viewModelScope.launch {
            launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
        }
    }

    companion object {
        private const val ANDROID_PLAY_STORE_PATH =
            "https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android"
    }
}
