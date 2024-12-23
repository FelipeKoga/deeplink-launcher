package dev.koga.deeplinklauncher.purchase

import Product
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoOpPurchaseApi : PurchaseApi {
    override val isAvailable: Boolean = false

    override fun init() {
    }

    override fun getProducts(): Flow<PersistentList<Product>> = flow {
    }

    override suspend fun purchase(product: Product): PurchaseResult {
        return PurchaseResult.Error(false)
    }

}