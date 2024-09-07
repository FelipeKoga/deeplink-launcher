package dev.koga.deeplinklauncher.model

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

actual val String.isLinkValid: Boolean
    get() {
        val nsurl = NSURL(string = this)

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        )

        return components?.scheme != null && components.host != null
    }
