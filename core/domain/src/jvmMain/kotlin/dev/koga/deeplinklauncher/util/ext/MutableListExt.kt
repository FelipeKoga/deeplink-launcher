package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.Target

fun MutableList<Target.Device>.addOrUpdate(device: Target.Device) {
    val index = indexOfFirst { it.name == device.name }

    if (index == -1) {
        add(device)
    } else {
        set(index, device)
    }
}