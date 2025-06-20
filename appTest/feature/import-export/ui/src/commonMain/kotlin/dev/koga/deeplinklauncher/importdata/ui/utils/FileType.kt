package dev.koga.deeplinklauncher.importdata.ui.utils

import dev.koga.deeplinklauncher.file.model.FileType

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
