package dev.koga.deeplinklauncher.screen.details.state

sealed interface DeepLinkDetailsEvent {
    data object Deleted : DeepLinkDetailsEvent
}
