package dev.koga.deeplinklauncher.settings.impl.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.purchase.api.Product
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.api.PurchaseResult
import dev.koga.deeplinklauncher.uievent.SnackBar
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val purchaseApi: PurchaseApi,
    private val appNavigator: AppNavigator,
    private val snackBarDispatcher: SnackBarDispatcher,
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
                    snackBarDispatcher.show(
                        SnackBar(
                            message = "Thank you for your support!",
                            variant = SnackBar.Variant.SUCCESS,
                        ),
                    )

                    appNavigator.popBackStack()
                }

                is PurchaseResult.Error -> {
                    if (!response.userCancelled) {
                        snackBarDispatcher.show(
                            SnackBar(
                                message = "Something went wrong, please try again",
                                variant = SnackBar.Variant.ERROR,
                            ),
                        )
                    }
                }
            }
        }
    }
}
