package dev.koga.deeplinklauncher.shared

import dev.koga.deeplinklauncher.devicebridge.di.deviceBridgeModule
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    includes(deviceBridgeModule)
}
