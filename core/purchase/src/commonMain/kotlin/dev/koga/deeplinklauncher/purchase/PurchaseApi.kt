package dev.koga.deeplinklauncher.purchase

import Product
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow

interface PurchaseApi {
    val isAvailable: Boolean

    fun init()
    fun getProducts(): Flow<PersistentList<Product>>
    suspend fun purchase(product: Product): PurchaseResult
}
