package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.event

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink


sealed interface DeepLinkDetailsEvent {
    data object Deleted : DeepLinkDetailsEvent
    data class Duplicated(val duplicatedDeepLink: DeepLink) : DeepLinkDetailsEvent
}
