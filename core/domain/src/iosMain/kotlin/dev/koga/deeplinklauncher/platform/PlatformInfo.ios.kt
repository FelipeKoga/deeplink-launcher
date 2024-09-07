package dev.koga.deeplinklauncher.platform

import platform.Foundation.NSBundle

actual class PlatformInfo {
    actual val version: String?
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String

    actual val storePath: String?
        get() = null
}
