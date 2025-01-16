package dev.koga.deeplinklauncher.importexport.model

enum class FileType(val extension: String) {
    JSON("application/json"),
    TXT("text/plain"),
    ;

    companion object {
        val extensions = entries.map { it.extension }
    }
}
