package dev.koga.deeplinklauncher.platform


expect val canShareContent: Boolean
expect val platform: Platform

enum class Platform {
    ANDROID,
    JVM,
}