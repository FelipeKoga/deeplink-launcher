package dev.koga.deeplinklauncher.provider

import java.util.UUID

actual object UUIDProvider {
    actual fun get(): String {
        return UUID.randomUUID().toString()
    }
}