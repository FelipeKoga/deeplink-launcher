package dev.koga.deeplinklauncher.util

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController

expect suspend fun shouldAskForPermission(
    permissionsController: PermissionsController,
    permission: Permission,
): Boolean
