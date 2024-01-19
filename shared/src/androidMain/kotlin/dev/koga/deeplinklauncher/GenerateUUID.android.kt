package dev.koga.deeplinklauncher

import java.util.UUID

actual object UUIDGenerator {
    actual fun generate(): String {
        return UUID.randomUUID().toString()
    }
}