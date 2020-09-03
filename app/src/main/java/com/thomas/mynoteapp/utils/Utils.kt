package com.thomas.mynoteapp.utils

import com.thomas.mynoteapp.utils.Utils.getFullDateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFullDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
    }

    fun getClearedUtc(): Calendar {
        val utc = Calendar.getInstance(TimeZone.getDefault())
        utc.clear()
        return utc
    }

    fun getTime(time: String, date: String): Long {
        val result = getFullDateFormat().parse("$date $time")
            ?: throw Exception("Parse time exception!")
        return result.time
    }

    fun getCurrentHour(): Int {
        val cal = getClearedUtc().apply {
            timeInMillis = System.currentTimeMillis()
        }
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentMinute(): Int {
        val cal = getClearedUtc().apply {
            timeInMillis = System.currentTimeMillis()
        }
        return cal.get(Calendar.MINUTE)
    }
}

fun Long.convertToTime(): String {
    val cal = Utils.getClearedUtc()
    cal.timeInMillis = this
    return "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
}

fun Long.convertToDate(): String {
    val cal = Utils.getClearedUtc()
    cal.timeInMillis = this
    return "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
}

fun Long.convertToDateTime(): String {
    val date = Date(this)
    return getFullDateFormat().format(date)
}

fun Long.isInFuture(): Boolean {
    return false
}

fun String.largerThan50Character(): Boolean {
    return this.length >= 50
}





