package dev.koga.deeplinklauncher.deeplink.api.application

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem

public interface EnrichDeepLinksForList {
    public suspend operator fun invoke(links: List<DeepLink>): List<DeepLinkListItem>
}
