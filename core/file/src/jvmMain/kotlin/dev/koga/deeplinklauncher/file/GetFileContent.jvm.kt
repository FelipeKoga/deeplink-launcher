package dev.koga.deeplinklauncher.file

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.File

@SingleIn(AppScope::class)
@Inject
actual class GetFileContent {
    actual operator fun invoke(path: String): String {
        return File(path).readText()
    }
}
