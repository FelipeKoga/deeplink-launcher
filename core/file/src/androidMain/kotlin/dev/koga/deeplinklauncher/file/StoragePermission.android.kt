package dev.koga.deeplinklauncher.file

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

actual class StoragePermission(
    private val context: Context,
) {

    actual fun request() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return
        }

        // TODO
    }

    actual fun isGranted(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return true
        }

        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        return ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }
}