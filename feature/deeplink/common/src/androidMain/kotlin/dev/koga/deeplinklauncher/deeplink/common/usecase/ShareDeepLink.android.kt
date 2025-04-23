package dev.koga.deeplinklauncher.deeplink.common.usecase

import android.content.Context
import android.content.Intent
import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink

internal class ShareDeepLinkImpl(
    private val context: Context,
) : ShareDeepLink {
    override fun invoke(deepLink: DeepLink) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, deepLink.link)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }
}
