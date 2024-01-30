package dev.koga.deeplinklauncher.model

enum class FileType(val mimeType: String, val extension: String) {
    JSON("application/json", "json"),
    TXT("text/plain", "txt"),
    ;

    companion object {
        val mimeTypes = entries.map { it.mimeType }
    }
}
