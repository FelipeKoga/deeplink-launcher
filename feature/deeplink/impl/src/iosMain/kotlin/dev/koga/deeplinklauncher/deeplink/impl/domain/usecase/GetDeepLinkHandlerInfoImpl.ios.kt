package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class GetDeepLinkHandlerInfoImpl : GetDeepLinkHandlerInfo {
    override fun invoke(link: String): DeepLinkHandlerInfo {
        val nsurl = NSURL(string = link)
        val canResolve = UIApplication.sharedApplication.canOpenURL(nsurl)

        return DeepLinkHandlerInfo(
            canResolve = canResolve,
            appName = null,
        )
    }
}
