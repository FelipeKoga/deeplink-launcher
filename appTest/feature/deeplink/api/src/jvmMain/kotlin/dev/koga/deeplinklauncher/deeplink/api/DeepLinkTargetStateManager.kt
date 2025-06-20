package dev.koga.deeplinklauncher.deeplink.api

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkTarget
import kotlinx.coroutines.flow.StateFlow

interface DeepLinkTargetStateManager {
    val targets: StateFlow<List<DeepLinkTarget>>
    val current: StateFlow<DeepLinkTarget>

    fun select(deeplinkTarget: DeepLinkTarget)
    fun next()
    fun prev()
}
