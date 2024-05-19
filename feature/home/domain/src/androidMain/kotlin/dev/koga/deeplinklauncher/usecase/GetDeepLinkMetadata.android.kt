package dev.koga.deeplinklauncher.usecase

import android.net.Uri
import dev.koga.deeplinklauncher.model.DeepLink

actual class GetDeepLinkMetadata {
    actual fun execute(deepLink: DeepLink): DeepLinkMetadata {
        val uri = Uri.parse(deepLink.link)

        return DeepLinkMetadata(
            deepLink = deepLink,
            scheme = uri.scheme,
            query = uri.query,
            host = uri.host,
        )
    }
}
