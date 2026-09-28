package dev.koga.deeplinklauncher.home.impl.ui

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.navigation.AppRoute

sealed interface HomeAction {
    data class LaunchDeepLink(val deepLink: DeepLink) : HomeAction
    data class ToggleFavorite(val deepLink: DeepLink) : HomeAction
    data class Search(val text: String) : HomeAction
    data class OnInputChanged(val text: String) : HomeAction
    data class OnSuggestionClicked(val suggestion: Suggestion) : HomeAction
    data class OnInputExpandedChanged(val expanded: Boolean) : HomeAction
    data class Navigate(val route: AppRoute) : HomeAction
    data class TabSelected(val tab: HomeTabPage) : HomeAction
    data object OnOnboardingShown : HomeAction
    data object LaunchInputDeepLink : HomeAction
}
