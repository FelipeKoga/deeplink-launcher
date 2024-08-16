package dev.koga.deeplinklauncher.datasource

import dev.koga.deeplinklauncher.model.Adb
import dev.koga.deeplinklauncher.model.Target
import dev.koga.deeplinklauncher.usecase.DeviceParser
import dev.koga.deeplinklauncher.util.ext.addOrUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class TargetDataSource(
    private val adb: Adb,
    private val parser: DeviceParser
) {

    private val _current = MutableStateFlow<Target>(Target.Browser)
    val current = _current.asStateFlow()

    private val _targets = MutableStateFlow(listOf<Target>(Target.Browser))
    val targets = _targets.asStateFlow()

    private fun update(targets: List<Target>) {

        _targets.value = targets

        _current.update { target ->
            targets.find {
                it == target
            } ?: Target.Browser
        }
    }

    fun select(target: Target) {
        _current.update {
            targets.value.find {
                it == target
            } ?: Target.Browser
        }
    }

    fun next() {
        val index = targets.value.indexOf(current.value)

        val next = targets.value.getOrNull(index = index + 1)
            ?: targets.value.first()

        _current.value = next
    }

    fun prev() {
        val index = targets.value.indexOf(current.value)

        val prev = targets.value.getOrNull(index = index - 1)
            ?: targets.value.last()

        _current.value = prev
    }

    fun track() = flow {

        if (!adb.installed) return@flow

        val devices = mutableListOf<Target.Device>()

        adb.trackDevices()
            .inputStream
            .bufferedReader()
            .useLines {
                it.forEach { line ->
                    devices.addOrUpdate(
                        parser(line).withName()
                    )

                    val targets =
                        listOf(Target.Browser) +
                                devices.filter { lines ->
                                    lines.active
                                }

                    emit(targets)
                }
            }
    }.onEach {
        update(it)
    }.flowOn(Dispatchers.IO)

    private fun Target.Device.withName(): Target.Device {

        return when (this) {
            is Target.Device.Emulator -> {
                copy(
                    name = adb.getEmulatorName(
                        target = this
                    ).ifEmpty {
                        serial
                    },
                )
            }

            is Target.Device.Physical -> {
                copy(
                    name = adb.getDeviceName(
                        target = this
                    ).ifEmpty {
                        adb.getDeviceModel(
                            target = this
                        ).ifEmpty {
                            serial
                        }
                    },
                )
            }
        }
    }
}