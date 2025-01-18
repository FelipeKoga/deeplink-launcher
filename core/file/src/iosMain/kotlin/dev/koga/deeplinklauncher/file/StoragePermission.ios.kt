package dev.koga.deeplinklauncher.file

actual class StoragePermission {

    actual fun request() {
    }

    actual fun isGranted(): Boolean {
        return true
    }
}
