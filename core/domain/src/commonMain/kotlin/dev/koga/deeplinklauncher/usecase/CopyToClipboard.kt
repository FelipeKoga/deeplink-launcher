package dev.koga.deeplinklauncher.usecase

expect class CopyToClipboard {
    fun copy(label: String, value: String)
}
