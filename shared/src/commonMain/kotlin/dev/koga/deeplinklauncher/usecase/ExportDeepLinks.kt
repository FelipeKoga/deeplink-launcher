package dev.koga.deeplinklauncher.usecase

expect class ExportDeepLinks {
    suspend fun export(type: FileType)
}

enum class FileType(val mimeType: String) {
    JSON("application/json"),
    TXT("text/plain")
}