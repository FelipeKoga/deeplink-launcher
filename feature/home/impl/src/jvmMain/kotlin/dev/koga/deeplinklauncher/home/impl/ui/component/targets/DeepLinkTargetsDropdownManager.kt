package dev.koga.deeplinklauncher.home.impl.ui.component.targets

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DeepLinkTargetsDropdownManager(
    private val stateManager: DeepLinkTargetStateManager,
    coroutineScope: CoroutineScope,
) {

    val uiState = combine(
        stateManager.current,
        stateManager.targets,
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = coroutineScope,
        initialValue = DeepLinkTargetsUiState(),
        started = SharingStarted.WhileSubscribed(),
    )

    fun select(target: DeepLinkTarget) {
        stateManager.select(target)
    }

    fun next() {
        stateManager.next()
    }

    fun prev() {
        stateManager.prev()
    }
}
