package dev.koga.deeplinklauncher

import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.DeeplinkTarget
import dev.koga.deeplinklauncher.util.ext.next
import dev.koga.deeplinklauncher.util.ext.previous
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class DeeplinkTargetStateManager internal constructor(
    deviceBridge: DeviceBridge,
    dispatcher: CoroutineDispatcher,
) {
    private val coroutineScope = CoroutineScope(dispatcher)

    private val devices = deviceBridge.track().map { devices ->
        devices.mapNotNull {
            if (it.active) {
                DeeplinkTarget.Device(
                    id = it.id,
                    name = it.name,
                    platform = it.platform,
                )
            } else {
                null
            }
        }
    }

    val targets: StateFlow<List<DeeplinkTarget>> = devices.mapLatest {
        listOf(DeeplinkTarget.Desktop) + it
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val _current = MutableStateFlow<DeeplinkTarget>(DeeplinkTarget.Desktop)
    val current = _current.asStateFlow()

    fun select(deeplinkTarget: DeeplinkTarget) {
        _current.update { deeplinkTarget }
    }

    fun next() {
        _current.update {
            targets.value.next(current.value)
        }
    }

    fun prev() {
        _current.update {
            targets.value.previous(current.value)
        }
    }
}
