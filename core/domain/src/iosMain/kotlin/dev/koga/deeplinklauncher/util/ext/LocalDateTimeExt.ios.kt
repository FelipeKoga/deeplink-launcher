package dev.koga.deeplinklauncher.util.ext

import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun LocalDateTime.format(format: String): String {
    val dateFormatter = NSDateFormatter().apply {
        dateFormat = format
        locale = NSLocale.currentLocale
    }

    val calendar = NSCalendar.currentCalendar
    val components = NSDateComponents().apply {
        year = this@format.year.toLong()
        month = this@format.monthNumber.toLong()
        day = this@format.dayOfMonth.toLong()
        hour = this@format.hour.toLong()
        minute = this@format.minute.toLong()
        second = this@format.second.toLong()
    }

    println("formata")
    val nsDate = calendar.dateFromComponents(components) ?: return ""
    println(nsDate)
    return dateFormatter.stringFromDate(nsDate).also { println(it) }
}
