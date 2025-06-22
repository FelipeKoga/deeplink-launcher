package dev.koga.deeplinklauncher.purchase.impl

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import dev.koga.deeplinklauncher.purchase.api.Product
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.api.PurchaseResult
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RevenueCatPurchaseApi : PurchaseApi {
    override val isAvailable = true

    override fun init() {
        Purchases.configure(apiKey = BuildKonfig.REVENUECAT_API_KEY)
    }

    override fun getProducts() = callbackFlow<PersistentList<Product>> {
        Purchases.sharedInstance.getOfferings(
            onError = { trySend(persistentListOf()) },
            onSuccess = { offerings ->
                val products = offerings.current?.availablePackages?.map {
                    Product(
                        title = it.storeProduct.title.replace(
                            regex = "\\(.*?\\)".toRegex(),
                            replacement = "",
                        ),
                        description = it.storeProduct.localizedDescription.orEmpty(),
                        formattedAmount = it.storeProduct.price.formatted,
                        amountMicros = it.storeProduct.price.amountMicros,
                        packageId = it.identifier,
                    )
                } ?: emptyList()

                trySend(products.sortedBy { it.amountMicros }.toPersistentList())
            },
        )

        awaitClose()
    }

    override suspend fun purchase(product: Product): PurchaseResult =
        suspendCoroutine { continuation ->
            Purchases.sharedInstance.getOfferings(
                onError = {},
                onSuccess = {
                    val productPackage = it.current?.getPackage(product.packageId)!!

                    Purchases.sharedInstance.purchase(
                        packageToPurchase = productPackage,
                        onSuccess = { _, _ ->
                            continuation.resume(PurchaseResult.Success)
                        },
                        onError = { _, userCancelled ->
                            continuation.resume(PurchaseResult.Error(userCancelled))
                        },
                    )
                },
            )
        }
}
