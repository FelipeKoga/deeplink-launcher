package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerInfo
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
