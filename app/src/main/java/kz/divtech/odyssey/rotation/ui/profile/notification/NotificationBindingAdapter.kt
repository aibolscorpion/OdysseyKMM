package kz.divtech.odyssey.rotation.ui.profile.notification

import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.HOUR_MINUTE_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatByGivenPattern
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.getLocalDateTimeByPattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object NotificationBindingAdapter {

    @BindingAdapter("updatedAt")
    @JvmStatic fun setUpdatedAt(textView: TextView, updatedAt: String?){
        val currentLocalDateTime = LocalDateTime.now()
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val updatedLocalDateTime = getLocalDateTimeByPattern(updatedAt!!)

        val date = formatByGivenPattern(updatedAt, DAY_MONTH_PATTERN)
        val time = formatByGivenPattern(updatedAt, HOUR_MINUTE_PATTERN)

        textView.text = when(updatedLocalDateTime.toLocalDate()){
            today -> {
                when(val minutes = updatedLocalDateTime.until(currentLocalDateTime, ChronoUnit.MINUTES)) {
                    in 0..60 -> App.appContext.getString(R.string.minutes_before, minutes)
                    in 60..120 -> App.appContext.getString(R.string.hours_before, 1)
                    in 120..180 -> App.appContext.getString(R.string.hours_before, 2)
                    else -> time
                }
            }
            yesterday -> App.appContext.getString(R.string.yesterday_at_time, time)
            else -> App.appContext.getString(R.string.date_at_time, date, time)
        }
    }

    @BindingAdapter("isImportant")
    @JvmStatic fun setIsImportant(textView: TextView, isImportant: Boolean){
        textView.text = if(isImportant)
            App.appContext.getString(R.string.important_notification)
        else
            App.appContext.getString(R.string.notification)
    }

    @BindingAdapter("importantButtonText")
    @JvmStatic fun setImportantButtonText(button: AppCompatButton, isImportant: Boolean){
        button.text = if(isImportant)
            App.appContext.getString(R.string.important_button_text)
        else
            App.appContext.getString(R.string.close)
    }

}