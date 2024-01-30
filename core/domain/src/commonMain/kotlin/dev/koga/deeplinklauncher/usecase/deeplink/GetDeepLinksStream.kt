package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class GetDeepLinksStream(
    private val dataSource: DeepLinkDataSource,
) {
    operator fun invoke() = dataSource.getDeepLinksStream()
}
