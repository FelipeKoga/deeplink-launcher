package dev.koga.deeplinklauncher.util

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController

actual suspend fun shouldAskForPermission(
    permissionsController: PermissionsController,
    permission: Permission
): Boolean {
    TODO("Not yet implemented")
}