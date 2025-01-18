package dev.koga.deeplinklauncher.file

expect class GetFileContent {
    operator fun invoke(path: String): String
}
