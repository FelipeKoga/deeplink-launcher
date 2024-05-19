package dev.koga.deeplinklauncher.screen.state

sealed interface HomeEvent {
    data object DeepLinksLaunched : HomeEvent
    data object ShowOnboarding : HomeEvent
}
