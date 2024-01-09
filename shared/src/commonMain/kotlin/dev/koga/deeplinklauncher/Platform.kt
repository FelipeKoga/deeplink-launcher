package dev.koga.deeplinklauncher

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform