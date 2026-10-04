package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import java.net.URI

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        return try {
            val uri = URI(link)
            uri.scheme != null
        } catch (e: Exception) {
            false
        }
    }
}
