package kz.divtech.odyssey.rotation.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeUtils {
    private const val SERVER_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    const val SERVER_DATE_PATTERN = "yyyy-MM-dd"
    const val DEFAULT_PATTERN = "d MMM yyyy, HH:mm"
    const val DAY_MONTH_DAY_OF_WEEK_PATTERN = "d MMM EE"
    const val DAY_MONTH_YEAR_PATTERN = "dd.MM.yyyy"
    const val DAY_MONTH_PATTERN = "d MMM"
    const val HOUR_MINUTE_PATTERN = "HH:mm"
    const val DAY_MONTH_HOUR_MINUTE_PATTERN = "d MMM HH:mm"

    fun String?.formatDateToGivenPattern(givenPattern: String): String{
        var formattedDate = ""
        this?.let {
            val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_DATE_PATTERN)
            val parsedDateTime = LocalDate.parse(this, serverDateTimeFormat)

            val format = DateTimeFormatter.ofPattern(givenPattern)
            formattedDate = parsedDateTime.format(format)
        }
        return formattedDate
    }

    fun String?.formatDateTimeToGivenPattern(givenPattern: String): String{
        var formattedDate = ""
        this?.let {
            val parsedDateTime = Instant.parse(this)
            val localDateTime = ZonedDateTime.ofInstant(parsedDateTime, ZoneId.of("UTC+6"))
            val format = DateTimeFormatter.ofPattern(givenPattern)
            formattedDate = localDateTime.format(format)
        }
        return formattedDate
    }

    fun String.getLocalDateTimeByPattern(): LocalDateTime {
        val parsedDateTime = Instant.parse(this)
        val localDateTime = ZonedDateTime.ofInstant(parsedDateTime, ZoneId.of("UTC+6"))
        return localDateTime.toLocalDateTime()
    }

    fun String.getLocalDateByPattern(): LocalDate {
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_DATE_PATTERN)
        return LocalDate.parse(this, serverDateTimeFormat)
    }

    fun Long.toDateString(): String {
        val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        val format = DateTimeFormatter.ofPattern("d MMM yyyy")
        return format.format(date)
    }
}