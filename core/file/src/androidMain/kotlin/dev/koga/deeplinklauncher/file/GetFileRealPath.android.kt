package dev.koga.deeplinklauncher.file

import android.content.Context
import android.net.Uri
import dev.koga.deeplinklauncher.file.ext.getRealPathFromUri
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
actual class GetFileRealPath(
    private val context: Context,
) {
    actual fun get(path: String): String {
        return Uri.parse(path).getRealPathFromUri(context)!!
    }
}
