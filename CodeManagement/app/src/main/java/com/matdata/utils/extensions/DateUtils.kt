
package com.matdata.utils.extensions

import com.matdata.BuildConfig
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String?.changeDateFormat(sourceDateFormat: String = "yyyy-MM-dd HH:mm:ss", targetDateFormat: String = "dd MMM, yyyy hh:mm a"): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val date = createDate(sourceDateFormat)
    return date.getStringDate(targetDateFormat)
}


fun Date?.getStringDate(format: String? = "yyyy-MM-dd"): String {
    if (this == null || format == null || format.isEmpty()) {
        return ""
    }
    val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return outputDateFormat.format(this)
}


fun String?.createDate(sourceFormat: String = "yyyy-MM-dd"): Date {
    if (this.isNullOrEmpty()) {
        return Date()
    }
    val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())
    var date = Date()
    try {
        date = inputDateFromat.parse(this)
    } catch (e: ParseException) {
        logStack(e)
    }

    return date
}


fun Long?.createDate(): Date {
    val inputDateFromat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date = Date()
    if (this == null) {
        try {
            date = inputDateFromat.parse(Date(this).toString())
        } catch (e: ParseException) {
            logStack(e)
        }
    }

    return date
}

private fun logStack(e: Exception) {
    if (BuildConfig.DEBUG) {
        e.printStackTrace()
    }
}

fun String?.getLocalTime(currentFormat: String = "yyyy-MM-dd HH:mm:ss", targetFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: String = "UTC"): String {
    if (this.isNullOrEmpty()) {
        return ""
    }

    val date = getLocalDateFromUtcDate(currentFormat, timeZone)

    return date.getStringDate(targetFormat)

}

fun String?.getLocalDateFromUtcDate(currentFormat: String, timeZone: String = "UTC"): Date? {
    if (isNullOrEmpty()) {
        return Date()
    }
    val simpleDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
    var myDate: Date? = Date()


    try {
        myDate = simpleDateFormat.parse(this)
    } catch (e: ParseException) {
        logStack(e)
    }

    return myDate
}
