package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal suspend fun DeepLinkRepository.recordLaunch(deepLink: DeepLink) {
    withContext(Dispatchers.IO + NonCancellable) {
        upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))
    }
}
