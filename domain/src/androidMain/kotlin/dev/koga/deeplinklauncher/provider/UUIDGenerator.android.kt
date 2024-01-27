package dev.koga.deeplinklauncher.provider

actual object UUIDGenerator {
    actual fun generate(): String {
        return UUIDGenerator.generate()
    }
}