package kz.divtech.odyssey.rotation.ui.profile.notification

import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate
import java.time.LocalTime

object NotificationBindingAdapter {

    @BindingAdapter("updatedAt")
    @JvmStatic fun setUpdatedAt(textView: TextView, updatedAt: String?){
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val currentLocalTime = LocalTime.now()

        val updatedLocalDateTime = Utils.getLocalDateTimeByPattern(updatedAt!!)
        val updatedLocalDate = updatedLocalDateTime.toLocalDate()
        val updatedLocalTime = updatedLocalDateTime.toLocalTime()

        val date = Utils.formatByGivenPattern(updatedAt, Utils.DAY_MONTH_PATTERN)
        val time = Utils.formatByGivenPattern(updatedAt, Utils.HOUR_MINUTE_PATTERN)

        textView.text = when(updatedLocalDate){
            today -> {
                when(updatedLocalTime.hour){
                    currentLocalTime.hour -> App.appContext.getString(R.string.minutes_before, updatedLocalTime.minute)
                    currentLocalTime.hour-1-> App.appContext.getString(R.string.hours_before, 1)
                    currentLocalTime.hour-2 -> App.appContext.getString(R.string.hours_before, 2)
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