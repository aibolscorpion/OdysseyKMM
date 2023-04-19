package kz.divtech.odyssey.rotation.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeUtils {
    const val SERVER_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val BIRTH_DATE_PATTERN = "yyyy-MM-dd"
    const val DEFAULT_PATTERN = "d MMM yyyy, HH:mm"
    const val DAY_MONTH_DAY_OF_WEEK_PATTERN = "d MMM EE"
    const val DAY_MONTH_YEAR_PATTERN = "d.MM.yyyy"
    const val DAY_MONTH_PATTERN = "d MMM"
    const val HOUR_MINUTE_PATTERN = "HH:mm"
    const val DAY_MONTH_HOUR_MINUTE_PATTERN = "d MMM HH:mm"

    fun formatByGivenPattern(dateTime: String?, pattern: String): String{
        var returnString = ""
        dateTime?.let {
            val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
            val parsedDateTime = LocalDateTime.parse(dateTime, serverDateTimeFormat)

            val format = DateTimeFormatter.ofPattern(pattern)
            returnString = parsedDateTime.format(format)
        }
        return returnString
    }


    fun getLocalDateTimeByPattern(serverDateTime: String): LocalDateTime {
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
        return LocalDateTime.parse(serverDateTime, serverDateTimeFormat)
    }

    fun getLocalDateByPattern(serverDateTime: String): LocalDate {
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
        return LocalDate.parse(serverDateTime, serverDateTimeFormat)
    }
}