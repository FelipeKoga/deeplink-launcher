package dev.koga.deeplinklauncher.purchase.impl.di

import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.impl.NoOpPurchaseApi
import org.koin.core.module.Module
import org.koin.dsl.module

actual val purchaseModule: Module = module {
    single<PurchaseApi> { NoOpPurchaseApi() }
}
