package dev.koga.deeplinklauncher.purchase.api

data class Product(
    val title: String,
    val description: String,
    val formattedAmount: String,
    val amountMicros: Long,
    val packageId: String,
) {
    companion object {
        val preview: Product = Product(
            title = "Title",
            description = "Description",
            formattedAmount = "$1.00",
            amountMicros = 0L,
            packageId = "packageId",
        )
    }
}
