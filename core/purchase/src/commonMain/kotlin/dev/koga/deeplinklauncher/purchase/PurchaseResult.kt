package dev.koga.deeplinklauncher.purchase

sealed interface PurchaseResult {
    data object Success : PurchaseResult
    data class Error(val userCancelled: Boolean) : PurchaseResult
}
