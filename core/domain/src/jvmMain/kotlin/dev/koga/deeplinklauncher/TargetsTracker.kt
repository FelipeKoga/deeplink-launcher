package dev.koga.deeplinklauncher

import dev.koga.deeplinklauncher.devicebridge.Adb
import dev.koga.deeplinklauncher.devicebridge.Xcrun
import dev.koga.deeplinklauncher.model.Target
import dev.koga.deeplinklauncher.util.ext.next
import dev.koga.deeplinklauncher.util.ext.previous
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TargetsTracker(
    adb: Adb,
    xcrun: Xcrun,
    dispatcher: CoroutineDispatcher,
) {
    private val coroutineScope = CoroutineScope(dispatcher)

    private val androidDevices = adb
        .track()
        .map { devices ->
            devices.map {
                Target.Device(
                    id = it.id,
                    name = it.name,
                    platform = Target.Device.Platform.ANDROID
                )
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val iOSDevices = xcrun
        .track()
        .map { devices ->
            devices.map {
                Target.Device(
                    id = it.id,
                    name = it.name,
                    platform = Target.Device.Platform.IOS
                )
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val targets: StateFlow<List<Target>> = combine(
        androidDevices,
        iOSDevices
    ) { androidDevices, iOSDevices ->
        listOf(Target.Browser) + androidDevices + iOSDevices
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    private val _current = MutableStateFlow<Target>(Target.Browser)
    val current = _current.asStateFlow()

    fun select(target: Target) {
        _current.update { target }
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
