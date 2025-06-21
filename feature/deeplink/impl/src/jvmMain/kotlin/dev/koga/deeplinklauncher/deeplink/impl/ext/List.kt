package dev.koga.deeplinklauncher.deeplink.impl.ext

internal fun <T> List<T>.next(current: T): T {
    val index = indexOf(current)

    return getOrNull(index + 1) ?: first()
}

internal fun <T> List<T>.previous(current: T): T {
    val index = indexOf(current)

    return getOrNull(index - 1) ?: last()
}
