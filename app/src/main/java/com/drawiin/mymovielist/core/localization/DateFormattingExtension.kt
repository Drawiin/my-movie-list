package com.drawiin.mymovielist.core.localization

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toLocalDate(): String {
    return runCatching {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateObj = parser.parse(this)
        dateObj?.let { DateFormat.getDateInstance(DateFormat.MEDIUM).format(it) } ?: this
    }.getOrElse { this }
}
