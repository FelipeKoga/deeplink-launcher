package dev.koga.deeplinklauncher.usecase.deeplink

import android.content.Context
import android.content.Intent
import dev.koga.deeplinklauncher.model.DeepLink

actual class ShareDeepLink(
    private val context: Context,
) {
    actual operator fun invoke(deepLink: DeepLink) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, deepLink.link)
        }

        val chooser = Intent.createChooser(intent, "Share Deep Link")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(chooser)
    }
}
