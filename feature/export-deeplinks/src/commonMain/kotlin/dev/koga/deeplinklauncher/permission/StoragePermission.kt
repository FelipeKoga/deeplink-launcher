package dev.koga.deeplinklauncher.permission

expect class StoragePermission {
    fun request()
    fun isGranted(): Boolean
}
