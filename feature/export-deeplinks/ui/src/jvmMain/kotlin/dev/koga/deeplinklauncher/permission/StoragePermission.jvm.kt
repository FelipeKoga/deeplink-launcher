package dev.koga.deeplinklauncher.permission

actual class StoragePermission {

    actual fun request() {
    }

    actual fun isGranted(): Boolean {
        return true
    }
}