package dev.koga.deeplinklauncher.home.ui.state

sealed interface HomeEvent {
    data object DeepLinksLaunched : HomeEvent
    data object ShowOnboarding : HomeEvent
}
