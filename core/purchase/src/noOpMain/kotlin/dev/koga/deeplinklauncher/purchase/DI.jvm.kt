package dev.koga.deeplinklauncher.purchase

import org.koin.core.module.Module
import org.koin.dsl.module

actual val purchaseModule: Module = module {
    single<PurchaseApi> { NoOpPurchaseApi() }
}
