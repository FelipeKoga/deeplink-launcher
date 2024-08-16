package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.Target

fun MutableList<Target.Device>.addOrUpdate(device: Target.Device) {
    val index = indexOfFirst { it.serial == device.serial }

    if (index == -1) {
        add(device)
    } else {
        set(index, device)
    }
}


fun <T> List<T>.next(current : T) : T {

    val index = indexOf(current)

    return getOrNull(index + 1) ?: first()
}

fun <T> List<T>.previous(current : T) : T {

    val index = indexOf(current)

    return getOrNull(index - 1) ?: last()
}