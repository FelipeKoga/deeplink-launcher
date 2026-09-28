package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.koga.deeplinklauncher.deeplink.uicomponent.formatting.truncateDeepLinkMiddle

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
