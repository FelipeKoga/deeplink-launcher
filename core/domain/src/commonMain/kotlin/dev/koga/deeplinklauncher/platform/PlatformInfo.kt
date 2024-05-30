package dev.koga.deeplinklauncher.platform

expect class PlatformInfo {
    val version: String?
    val storePath: String?
}

const val androidPlayStorePath =
    "https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android"
