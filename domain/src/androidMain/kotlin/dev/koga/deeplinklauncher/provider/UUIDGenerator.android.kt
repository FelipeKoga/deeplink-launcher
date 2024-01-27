package dev.koga.deeplinklauncher.provider

actual object UUIDProvider {
    actual fun provide(): String {
        return UUIDProvider.provide()
    }
}