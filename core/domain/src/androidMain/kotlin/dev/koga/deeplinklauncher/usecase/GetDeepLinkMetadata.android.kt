package dev.koga.deeplinklauncher.usecase

import android.net.Uri

actual class GetDeepLinkMetadata {
    actual fun execute(link: String): DeepLinkMetadata {
        val uri = Uri.parse(link)

        return DeepLinkMetadata(
            link = link,
            scheme = uri.scheme,
            query = uri.query,
            host = uri.host,
        )
    }
}
