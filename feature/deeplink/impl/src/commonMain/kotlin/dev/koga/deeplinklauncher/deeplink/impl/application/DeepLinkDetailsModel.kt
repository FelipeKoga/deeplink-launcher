package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.ui.formatting.truncateDeepLinkMiddle
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkMetadata
internal data class DeepLinkDetailsModel(
    val deepLink: DeepLink,
    val metadata: DeepLinkMetadata,
    val handlerInfo: DeepLinkHandlerInfo,
    val icon: DeepLinkIcon? = null,
) {
    val displayName: String
        get() = deepLink.name?.takeIf { it.isNotBlank() }
            ?: metadata.host
            ?: deepLink.link.truncateDeepLinkMiddle()
}
