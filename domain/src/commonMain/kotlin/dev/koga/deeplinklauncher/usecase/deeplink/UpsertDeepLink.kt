package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink

class UpsertDeepLink(
    private val dataSource: DeepLinkDataSource
) {
    operator fun invoke(deepLink: DeepLink) =
        dataSource.upsertDeepLink(deepLink)
}