package dev.koga.deeplinklauncher.deeplink.uicomponent

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.uicomponent.model.DeepLinkListItem

public interface EnrichDeepLinksForList {
    public suspend operator fun invoke(links: List<DeepLink>): List<DeepLinkListItem>
}
