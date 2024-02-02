package dev.koga.deeplinklauncher.model

enum class ExportFileType(val label: String) {
    JSON("JSON (.json)"),
    PLAIN_TEXT("Plain text (.txt)"),
    ;

    companion object {
        fun getByLabel(label: String): ExportFileType {
            return entries.first { it.label == label }
        }
    }
}
