package dev.koga.deeplinklauncher.file

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
actual class StoragePermission {

    actual fun request() {
    }

    actual fun isGranted(): Boolean {
        return true
    }
}
