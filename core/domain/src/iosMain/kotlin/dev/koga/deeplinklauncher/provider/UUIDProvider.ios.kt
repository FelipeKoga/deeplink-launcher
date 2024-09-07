package dev.koga.deeplinklauncher.provider

import platform.Foundation.NSUUID

actual object UUIDProvider {
    actual fun get(): String = NSUUID.UUID().UUIDString()
}
