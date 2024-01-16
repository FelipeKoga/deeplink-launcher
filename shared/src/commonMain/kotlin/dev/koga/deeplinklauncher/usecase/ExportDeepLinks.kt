package dev.koga.deeplinklauncher.usecase

expect class ExportDeepLinks {
    suspend fun export(type: FileType): ExportDeepLinksOutput
}

sealed interface ExportDeepLinksOutput {
    data object Success : ExportDeepLinksOutput
    data object Empty : ExportDeepLinksOutput
    data class Error(val throwable: Throwable) : ExportDeepLinksOutput
}

enum class FileType(val mimeType: String) {
    JSON("application/json"),
    TXT("text/plain")
}