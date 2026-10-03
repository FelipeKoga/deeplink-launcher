package dev.koga.deeplinklauncher.shared.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal interface KoinBridge

internal fun KoinBridge.koinBridgeModule(): Module = module { }
