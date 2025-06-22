package dev.koga.deeplinklauncher.purchase.impl.di

import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.purchase.impl.RevenueCatPurchaseApi
import org.koin.core.module.Module
import org.koin.dsl.module

actual val purchaseModule: Module = module {
    single<PurchaseApi> { RevenueCatPurchaseApi() }
}
