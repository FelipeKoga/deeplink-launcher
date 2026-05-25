package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState

internal class CaptureDeviceStateImpl : CaptureDeviceState {
    override suspend fun invoke(deviceId: String): CaptureDeviceState.Result {
        return CaptureDeviceState.Result.Failure("Capture is only available on Desktop with ADB")
    }
}
