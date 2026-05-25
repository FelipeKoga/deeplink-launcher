package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.AddDeepLinkToShortcuts
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ExportBatchTestReport
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetSelectedAndroidDeviceId
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.VerifyDeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.impl.domain.manager.DeepLinkTargetStateManagerImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.AddDeepLinkToShortcutsImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.CaptureDeviceStateImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.ExportBatchTestReportImpl
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.RunFolderBatchTest
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetSelectedAndroidDeviceIdImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetDeepLinkFromClipboard
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetDeepLinkHandlerIconImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetDeepLinkHandlerInfoImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetDeepLinkMetadataImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.LaunchDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.PinDeepLinkToHomeScreenImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.RunFolderBatchTestImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.ShareDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.ValidateDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.VerifyDeepLinkAssertionImpl
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.FolderBatchTestViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::GetDeepLinkMetadataImpl) bind GetDeepLinkMetadata::class
    singleOf(::GetDeepLinkHandlerInfoImpl) bind GetDeepLinkHandlerInfo::class
    singleOf(::GetDeepLinkHandlerIconImpl) bind GetDeepLinkHandlerIcon::class
    singleOf(::LaunchDeepLinkImpl) bind LaunchDeepLink::class
    singleOf(::ValidateDeepLinkImpl) bind ValidateDeepLink::class
    singleOf(::ShareDeepLinkImpl) bind ShareDeepLink::class
    singleOf(::PinDeepLinkToHomeScreenImpl) bind PinDeepLinkToHomeScreen::class
    singleOf(::AddDeepLinkToShortcutsImpl) bind AddDeepLinkToShortcuts::class
    singleOf(::CaptureDeviceStateImpl) bind CaptureDeviceState::class
    singleOf(::GetSelectedAndroidDeviceIdImpl) bind GetSelectedAndroidDeviceId::class
    singleOf(::VerifyDeepLinkAssertionImpl) bind VerifyDeepLinkAssertion::class
    singleOf(::RunFolderBatchTestImpl) bind RunFolderBatchTest::class
    singleOf(::ExportBatchTestReportImpl) bind ExportBatchTestReport::class
    singleOf(::GetDeepLinkFromClipboard)

    viewModelOf(::FolderBatchTestViewModel)

    single {
        DeepLinkTargetStateManagerImpl(
            deviceBridge = get(),
            dispatcher = Dispatchers.IO,
        )
    } bind DeepLinkTargetStateManager::class
}
