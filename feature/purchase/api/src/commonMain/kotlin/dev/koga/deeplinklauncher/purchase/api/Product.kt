package dev.koga.deeplinklauncher.purchase.api

data class Product(
    val title: String,
    val description: String,
    val formattedAmount: String,
    val amountMicros: Long,
    val packageId: String,
)
