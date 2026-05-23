package dev.koga.deeplinklauncher.deeplink.api.ui.model

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public data class DeepLinkListItem(
    val deepLink: DeepLink,
    val icon: DeepLinkIcon? = null,
)
