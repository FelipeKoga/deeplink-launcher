package dev.koga.deeplinklauncher.datatransfer.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

interface ExportDeepLinks {
    operator fun invoke(type: FileType): Result

    sealed interface Result {
        data class Success(val fileName: String) : Result
        data object Empty : Result
        data class Error(val throwable: Throwable) : Result
    }
}
