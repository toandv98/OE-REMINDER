package com.edu.sun.oereminder.utils

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "dd MMMM"
const val DATE_FORMAT_DAY_OF_WEEK = "EEEE, dd-MM-yyyy"
const val TIME_FORMAT = "hh:mm"
const val TIME_NULL = "--:--"

fun GregorianCalendar.toDate(): String =
    SimpleDateFormat(DATE_FORMAT_DAY_OF_WEEK, Locale.getDefault()).format(time)

fun GregorianCalendar.toTime(): String =
    if (timeInMillis == 0L) TIME_NULL else SimpleDateFormat(
        TIME_FORMAT,
        Locale.getDefault()
    ).format(time)

fun GregorianCalendar.toDate(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(time)

fun GregorianCalendar.firstDayOfMonth() = this.apply {
    set(Calendar.DATE, getActualMinimum(Calendar.DATE))
}

fun GregorianCalendar.lastDayOfMonth() = this.apply {
    set(Calendar.DATE, getActualMaximum(Calendar.DATE))
}
