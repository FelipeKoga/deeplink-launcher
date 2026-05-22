package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream

internal fun Context.resolveHandlerIconBitmap(link: String): Bitmap? {
    val intent = Intent(Intent.ACTION_VIEW, link.trim().toUri())
    val drawable = packageManager.resolveActivity(intent, 0)?.loadIcon(packageManager) ?: return null
    return drawable.toBitmap()
}

internal fun Context.resolveHandlerIconPng(link: String): ByteArray? {
    val bitmap = resolveHandlerIconBitmap(link) ?: return null
    return bitmap.toPngByteArray()
}

internal fun Context.resolveShortcutIcon(intent: Intent): Icon? {
    val drawable = packageManager.resolveActivity(intent, 0)?.loadIcon(packageManager) ?: return null
    return Icon.createWithBitmap(drawable.toBitmap())
}

internal fun Context.resolveShortcutIconCompat(intent: Intent): IconCompat? {
    val drawable = packageManager.resolveActivity(intent, 0)?.loadIcon(packageManager) ?: return null
    return IconCompat.createWithBitmap(drawable.toBitmap())
}

private fun Bitmap.toPngByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
