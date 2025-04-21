package dev.koga.deeplinklauncher.settings.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.purchase.api.Product
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.api.PurchaseResult
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val purchaseApi: PurchaseApi,
) : ViewModel() {

    val products = purchaseApi.getProducts().stateIn(
        scope = viewModelScope,
        initialValue = persistentListOf(),
        started = SharingStarted.WhileSubscribed(),
    )

    fun purchase(product: Product) {
        viewModelScope.launch {
            when (val response = purchaseApi.purchase(product)) {
                PurchaseResult.Success -> {
//                    messageDispatcher.send("Thank you for your support!")
                }

                is PurchaseResult.Error -> {
                    if (!response.userCancelled) {
//                        messageDispatcher.send("Something went wrong, please try again")
                    }
                }
            }
        }
    }
}
