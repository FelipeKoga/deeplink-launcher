package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal suspend fun DeepLinkRepository.recordLaunch(deepLink: DeepLink) {
    withContext(NonCancellable) {
        recordLaunch(id = deepLink.id, launchedAt = currentLocalDateTime)
    }
}
