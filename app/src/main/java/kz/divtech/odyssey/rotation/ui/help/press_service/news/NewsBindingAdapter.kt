package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate

object NewsBindingAdapter {

    @BindingAdapter("publishedDate")
    @JvmStatic fun setPublishedDate(textView: TextView, publishedDate: String){
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val date = Utils.formatByGivenPattern(publishedDate, Utils.DAY_MONTH_PATTERN)
        val time = Utils.formatByGivenPattern(publishedDate, Utils.HOUR_MINUTE_PATTERN)

        textView.text = when(Utils.getLocalDateByPattern(publishedDate)){
            today -> App.appContext.getString(R.string.today_at_time, time)
            yesterday -> App.appContext.getString(R.string.yesterday_at_time, time)
            else -> App.appContext.getString(R.string.date_at_time, date, time)
        }
    }
}