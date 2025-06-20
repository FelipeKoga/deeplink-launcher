package dev.koga.deeplinklauncher.file.ext

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

fun Uri.getRealPathFromUri(context: Context): String? {
    if (this.scheme.equals("file")) {
        return this.path
    }

    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nameIndex != -1) {
            val fileName = cursor.getString(nameIndex)
            return copyFileToInternalStorage(context, this, fileName)
        }
    }
    return null
}

fun copyFileToInternalStorage(context: Context, uri: Uri, fileName: String): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val newFile = File(context.filesDir, fileName)
    inputStream.use { input ->
        FileOutputStream(newFile).use { output ->
            input?.copyTo(output)
        }
    }
    return newFile.path
}
