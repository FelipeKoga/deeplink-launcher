package dev.koga.deeplinklauncher.deeplink.uicomponent.model

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon

public data class DeepLinkListItem(
    val deepLink: DeepLink,
    val icon: DeepLinkIcon? = null,
    val handlerAppName: String? = null,
)
