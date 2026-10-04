package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class GetDeepLinksPlainTextPreviewImpl(
    private val repository: DeepLinkRepository,
) : GetDeepLinksPlainTextPreview {

    override operator fun invoke(): String {
        val deepLinks = repository.getDeepLinks()

        return deepLinks.joinToString(separator = "\n") { deepLink ->
            deepLink.link
        }
    }
}
