package dev.koga.deeplinklauncher.platform

expect class GetFileContent {
    operator fun invoke(path: String): String
}