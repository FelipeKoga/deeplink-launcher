package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class GetDeepLinkByLink(
    private val dataSource: DeepLinkDataSource,
) {
    operator fun invoke(link: String) = dataSource.getDeepLinkByLink(link)
}
