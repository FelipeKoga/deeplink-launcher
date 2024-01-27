package dev.koga.deeplinklauncher.provider

expect object UUIDProvider {
    fun provide(): String
}