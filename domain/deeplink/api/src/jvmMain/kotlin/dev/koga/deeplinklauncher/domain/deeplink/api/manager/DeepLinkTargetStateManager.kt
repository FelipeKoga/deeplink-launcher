package dev.koga.deeplinklauncher.domain.deeplink.api.manager
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkTarget
import kotlinx.coroutines.flow.StateFlow

public interface DeepLinkTargetStateManager {
    public val targets: StateFlow<List<DeepLinkTarget>>
    public val current: StateFlow<DeepLinkTarget>

    public fun select(deeplinkTarget: DeepLinkTarget)
    public fun next()
    public fun prev()
}
