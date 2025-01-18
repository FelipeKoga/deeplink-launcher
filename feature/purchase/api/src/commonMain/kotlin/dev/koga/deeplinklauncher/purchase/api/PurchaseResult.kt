package dev.koga.deeplinklauncher.purchase.api

sealed interface PurchaseResult {
    data object Success : PurchaseResult
    data class Error(val userCancelled: Boolean) : PurchaseResult
}
