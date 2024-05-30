package dev.koga.deeplinklauncher.file

import android.content.Context
import android.net.Uri
import dev.koga.deeplinklauncher.util.ext.getRealPathFromUri

actual class GetFileRealPath(
    private val context: Context,
) {
    actual fun get(path: String): String {
        return Uri.parse(path).getRealPathFromUri(context)!!
    }
}
