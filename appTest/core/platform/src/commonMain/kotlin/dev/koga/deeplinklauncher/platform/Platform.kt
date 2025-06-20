package dev.koga.deeplinklauncher.platform

enum class Platform {
    ANDROID,
    IOS,
    JVM,
}

expect val currentPlatform: Platform
expect val canShareContent: Boolean
