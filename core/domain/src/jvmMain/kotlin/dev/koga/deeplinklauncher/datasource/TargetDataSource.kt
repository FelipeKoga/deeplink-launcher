package dev.koga.deeplinklauncher.datasource

import dev.koga.deeplinklauncher.manager.AdbManager
import dev.koga.deeplinklauncher.model.Target
import dev.koga.deeplinklauncher.usecase.GetDeviceType
import dev.koga.deeplinklauncher.util.ext.addOrUpdate
import dev.koga.deeplinklauncher.util.ext.next
import dev.koga.deeplinklauncher.util.ext.previous
import dev.koga.deeplinklauncher.util.ext.useProtoText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class TargetDataSource(
    private val adbManager: AdbManager,
    private val getDeviceType: GetDeviceType,
) {
    private val targets = MutableStateFlow(listOf<Target>(Target.Browser))

    private val _current = MutableStateFlow<Target>(Target.Browser)
    val current = _current.asStateFlow()

    private fun update(targets: List<Target>) {
        this.targets.update { targets }

        _current.update { target ->
            targets.find { it == target } ?: Target.Browser
        }
    }

    fun select(target: Target) {
        _current.update {
            targets.value.find { it == target } ?: Target.Browser
        }
    }

    fun next() {
        _current.value = targets.value.next(current.value)
    }

    fun prev() {
        _current.value = targets.value.previous(current.value)
    }

    fun track() = flow {
        if (!adbManager.installed) return@flow

        val devices = mutableListOf<Target.Device>()

        adbManager.trackDevices()
            .inputStream
            .bufferedReader()
            .useProtoText(target = "device") { deviceProtoText ->

                devices.addOrUpdate(
                    getDeviceType(deviceProtoText),
                )

                emit(
                    listOf(
                        Target.Browser,
                        *devices.filter { device ->
                            device.active
                        }.toTypedArray(),
                    ),
                )
            }
    }.onEach {
        update(it)
    }
}
