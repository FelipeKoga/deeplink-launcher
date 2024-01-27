package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class DeleteDeepLink(
    private val dataSource: DeepLinkDataSource
) {
    operator fun invoke(id: String) = dataSource.deleteDeepLink(id)
}