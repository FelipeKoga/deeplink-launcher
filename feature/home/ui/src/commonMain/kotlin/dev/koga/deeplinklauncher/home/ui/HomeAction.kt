package dev.koga.deeplinklauncher.home.ui

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute

sealed interface HomeAction {
    data class LaunchDeepLink(val deepLink: DeepLink) : HomeAction
    data class AddFolder(val name: String, val description: String) : HomeAction
    data class Search(val text: String) : HomeAction
    data class OnInputChanged(val text: String) : HomeAction
    data class OnSuggestionClicked(val suggestion: Suggestion) : HomeAction
    data class Navigate(val route: AppNavigationRoute) : HomeAction
    data object OnOnboardingShown : HomeAction
    data object LaunchInputDeepLink : HomeAction
}