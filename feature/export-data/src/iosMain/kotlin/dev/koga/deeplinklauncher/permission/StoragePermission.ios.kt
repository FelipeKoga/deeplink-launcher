package dev.koga.deeplinklauncher.permission

import platform.Photos.PHPhotoLibrary
import platform.Photos.PHAuthorizationStatusAuthorized

actual class StoragePermission {

    actual fun request() {
        // No iOS, as permissões para acessar fotos devem ser solicitadas, se necessário.
    }

    actual fun isGranted(): Boolean {
        return true
    }
}