package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.SaveDeepLinkAssertion

internal class SaveDeepLinkAssertionImpl : SaveDeepLinkAssertion {
    override fun invoke(deepLinkId: String, assertion: DeepLinkAssertion?) = Unit
}
