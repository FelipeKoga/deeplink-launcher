package dev.koga.deeplinklauncher.file

expect class StoragePermission {
    fun request()
    fun isGranted(): Boolean
}
