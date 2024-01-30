package dev.koga.deeplinklauncher.usecase

expect class GetFileContent {
    operator fun invoke(path: String): String
}
