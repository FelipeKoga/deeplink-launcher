package dev.koga.deeplinklauncher.file.model

enum class FileType(val mimeType: String, val extension: String) {
    JSON("application/json", "json"),
    TXT("text/plain", "txt"),
    ;

    companion object {
        val extensions = entries.map { it.extension }
    }
}

val FileType.label
    get() = when (this) {
        FileType.JSON -> "JSON (.json)"
        FileType.TXT -> "Plain text (.txt)"
    }

fun FileType.Companion.getByLabel(label: String): FileType {
    return when {
        FileType.JSON.label == label -> FileType.JSON
        else -> FileType.TXT
    }
}
