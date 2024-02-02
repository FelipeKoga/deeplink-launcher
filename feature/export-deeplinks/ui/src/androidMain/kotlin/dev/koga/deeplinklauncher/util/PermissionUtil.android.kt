package dev.koga.deeplinklauncher.util

import android.os.Build
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController

actual suspend fun shouldAskForPermission(
    permissionsController: PermissionsController,
    permission: Permission,
): Boolean {
    return !permissionsController.isPermissionGranted(permission) &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}
