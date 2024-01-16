package dev.koga.deeplinklauncher.usecase

expect class ImportDeepLinks {
    fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput
}

sealed interface ImportDeepLinksOutput {
    data object Success : ImportDeepLinksOutput
    data class Error(val throwable: Throwable) : ImportDeepLinksOutput
}