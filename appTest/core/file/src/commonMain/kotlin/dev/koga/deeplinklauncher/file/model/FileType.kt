package dev.koga.deeplinklauncher.file.model

enum class FileType(val mimeType: String, val extension: String) {
    JSON("application/json", "json"),
    TXT("text/plain", "txt"),
    ;

    companion object {
        val extensions = entries.map { it.extension }
    }
}
