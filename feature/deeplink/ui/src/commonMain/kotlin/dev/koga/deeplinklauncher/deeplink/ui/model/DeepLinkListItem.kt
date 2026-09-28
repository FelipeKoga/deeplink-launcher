package dev.koga.deeplinklauncher.deeplink.ui.model
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
public data class DeepLinkListItem(
    val deepLink: DeepLink,
    val icon: DeepLinkIcon? = null,
    val handlerAppName: String? = null,
)
