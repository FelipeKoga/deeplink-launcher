package dev.koga.deeplinklauncher.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.net.URI

fun String.isUriValid(): Boolean {
    return try {
        val uri = URI(this)
        uri.scheme != null && uri.host != null
    } catch (e: Throwable) {
        false
    }
}

fun Uri.getRealPathFromUri(context: Context): String? {
    // Check if the URI is a 'file' URI - if so, return the path directly
    if (this.scheme.equals("file")) {
        return this.path
    }

    // For 'content' URIs, try to read the file name from the media store
    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        /*
         * Use OpenableColumns to get the name of the file, as the path is not directly accessible.
         * You might need to copy the content to your application's storage space to work with it as a regular file.
         */
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nameIndex != -1) {
            val fileName = cursor.getString(nameIndex)
            // You can use this file name to create a copy in your app's internal storage
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
