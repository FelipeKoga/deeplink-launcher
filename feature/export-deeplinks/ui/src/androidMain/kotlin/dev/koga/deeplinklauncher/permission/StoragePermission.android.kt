package dev.koga.deeplinklauncher.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

actual class StoragePermission(
    private val context: Context,
) {

    actual fun request() {
    }

    actual fun isGranted(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        return ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }
}
