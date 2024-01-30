package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class GetDeepLinkById(
    private val dataSource: DeepLinkDataSource,
) {
    operator fun invoke(id: String) = dataSource.getDeepLinkById(id)
}
