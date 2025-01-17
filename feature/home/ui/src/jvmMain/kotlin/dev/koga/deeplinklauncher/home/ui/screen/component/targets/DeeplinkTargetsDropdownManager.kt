package dev.koga.deeplinklauncher.home.ui.screen.component.targets

import dev.koga.deeplinklauncher.devicebridge.DeeplinkTargetStateManager
import dev.koga.deeplinklauncher.devicebridge.model.DeeplinkTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DeeplinkTargetsDropdownManager(
    private val deeplinkTargetStateManager: DeeplinkTargetStateManager,
    coroutineScope: CoroutineScope,
) {

    val uiState = combine(
        deeplinkTargetStateManager.current,
        deeplinkTargetStateManager.targets,
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = coroutineScope,
        initialValue = DeeplinkTargetsUiState(),
        started = SharingStarted.WhileSubscribed(),
    )

    fun select(deeplinkTarget: DeeplinkTarget) {
        deeplinkTargetStateManager.select(deeplinkTarget)
    }

    fun next() {
        deeplinkTargetStateManager.next()
    }

    fun prev() {
        deeplinkTargetStateManager.prev()
    }
}
