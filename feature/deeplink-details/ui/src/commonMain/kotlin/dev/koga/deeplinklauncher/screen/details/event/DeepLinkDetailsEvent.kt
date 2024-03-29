package dev.koga.deeplinklauncher.screen.details.event

import dev.koga.deeplinklauncher.model.DeepLink

sealed interface DeepLinkDetailsEvent {
    data object Deleted : DeepLinkDetailsEvent
    data class Duplicated(val duplicatedDeepLink: DeepLink) : DeepLinkDetailsEvent
}
