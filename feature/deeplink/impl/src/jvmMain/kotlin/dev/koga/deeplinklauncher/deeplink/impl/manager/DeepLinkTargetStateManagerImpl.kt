package dev.koga.deeplinklauncher.deeplink.impl.manager

import dev.koga.deeplinklauncher.deeplink.api.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkTarget
import dev.koga.deeplinklauncher.deeplink.impl.ext.next
import dev.koga.deeplinklauncher.deeplink.impl.ext.previous
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
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
internal class DeepLinkTargetStateManagerImpl(
    deviceBridge: DeviceBridge,
    dispatcher: CoroutineDispatcher,
) : DeepLinkTargetStateManager {
    private val coroutineScope = CoroutineScope(dispatcher)

    private val devices = deviceBridge.track().map { devices ->
        devices.mapNotNull {
            if (it.active) {
                DeepLinkTarget.Device(
                    id = it.id,
                    name = it.name,
                    platform = it.platform,
                )
            } else {
                null
            }
        }
    }

    override val targets: StateFlow<List<DeepLinkTarget>> = devices.mapLatest {
        listOf(DeepLinkTarget.Desktop) + it
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val _current = MutableStateFlow<DeepLinkTarget>(DeepLinkTarget.Desktop)
    override val current = _current.asStateFlow()

    override fun select(deeplinkTarget: DeepLinkTarget) {
        _current.update { deeplinkTarget }
    }

    override fun next() {
        _current.update {
            targets.value.next(current.value)
        }
    }

    override fun prev() {
        _current.update {
            targets.value.previous(current.value)
        }
    }
}
