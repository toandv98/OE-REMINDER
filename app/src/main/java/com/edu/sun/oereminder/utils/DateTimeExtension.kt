package com.edu.sun.oereminder.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val DATE_FORMAT = "dd MMMM"
const val DATE_FORMAT_DAY_OF_WEEK = "EEEE, dd-MM-yyyy"
const val TIME_FORMAT = "HH:mm"
const val TIME_NULL = "--:--"

fun GregorianCalendar.toDate() =
    SimpleDateFormat(DATE_FORMAT_DAY_OF_WEEK, Locale.getDefault()).format(time).toString()

fun GregorianCalendar.toTime() =
    if (timeInMillis == 0L) TIME_NULL else SimpleDateFormat(
        TIME_FORMAT,
        Locale.getDefault()
    ).format(time).toString()

fun GregorianCalendar.toDate(pattern: String) =
    SimpleDateFormat(pattern, Locale.getDefault()).format(time).toString()

fun GregorianCalendar.firstDayOfMonth() = this.apply {
    set(Calendar.DATE, getActualMinimum(Calendar.DATE))
}

fun GregorianCalendar.lastDayOfMonth() = this.apply {
    set(Calendar.DATE, getActualMaximum(Calendar.DATE))
}

fun GregorianCalendar.from(date: Long) = this.apply { timeInMillis = date }

fun firstMillisOfMonth() = GregorianCalendar().firstDayOfMonth().timeInMillis

fun lastMillisOfMonth() = GregorianCalendar().lastDayOfMonth().timeInMillis

fun firstMillisOfDay() = GregorianCalendar().firstMillisOfDay()

fun lastMillisOfDay() = GregorianCalendar().lastMillisOfDay()

fun GregorianCalendar.firstMillisOfDay() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

fun GregorianCalendar.lastMillisOfDay() = this.apply {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}.timeInMillis

fun millisUtil(hour: Long, minus: Long) =
    firstMillisOfDay().plus(TimeUnit.HOURS.toMillis(hour)).plus(TimeUnit.MINUTES.toMillis(minus))
