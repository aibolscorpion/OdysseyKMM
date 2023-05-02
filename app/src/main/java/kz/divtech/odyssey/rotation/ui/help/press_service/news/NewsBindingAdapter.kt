package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.HOUR_MINUTE_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatByGivenPattern
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.getLocalDateByPattern
import java.time.LocalDate

object NewsBindingAdapter {

    @BindingAdapter("publishedDate")
    @JvmStatic fun setPublishedDate(textView: TextView, publishedDate: String?){
        publishedDate?.let {
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)

            val date = formatByGivenPattern(publishedDate, DAY_MONTH_PATTERN)
            val time = formatByGivenPattern(publishedDate, HOUR_MINUTE_PATTERN)

            textView.text = when(publishedDate.getLocalDateByPattern()){
                today -> App.appContext.getString(R.string.today_at_time, time)
                yesterday -> App.appContext.getString(R.string.yesterday_at_time, time)
                else -> App.appContext.getString(R.string.date_at_time, date, time)
            }
        }
    }
}