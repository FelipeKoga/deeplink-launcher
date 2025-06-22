package dev.koga.deeplinklauncher.datatransfer.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

interface ImportDeepLinks {
    suspend operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): Result

    sealed interface Result {
        data object Success : Result

        sealed interface Error : Result {
            data class InvalidDeepLinksFound(val invalidDeepLinks: List<String>) : Error
            data object Unknown : Error
        }
    }
}
