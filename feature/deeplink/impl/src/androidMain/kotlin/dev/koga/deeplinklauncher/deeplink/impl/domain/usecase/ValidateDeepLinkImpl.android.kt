package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.net.Uri
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        return try {
            val uri = Uri.parse(link)
            uri.scheme != null
        } catch (e: Exception) {
            false
        }
    }
}
