package dev.koga.deeplinklauncher.file

import java.io.File

actual class GetFileContent {
    actual operator fun invoke(path: String): String {
        return File(path).readText()
    }
}
