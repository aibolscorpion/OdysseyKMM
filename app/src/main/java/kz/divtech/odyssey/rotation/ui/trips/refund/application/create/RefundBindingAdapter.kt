package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.domain.model.trips.Place
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils

object RefundBindingAdapter {

    @BindingAdapter("ticketDepDateTime", "ticketArrDateTime")
    @JvmStatic
    fun setTicketDepArrDateTime(textView: TextView, ticketDepDateTime: String?, ticketArrDateTime: String?) {
        val formattedDepDateTime = LocalDateTimeUtils.formatByGivenPattern(ticketDepDateTime,
            LocalDateTimeUtils.DAY_MONTH_HOUR_MINUTE_PATTERN)

        val formattedArrDateTime = LocalDateTimeUtils.formatByGivenPattern(ticketArrDateTime,
            LocalDateTimeUtils.DAY_MONTH_HOUR_MINUTE_PATTERN)

        textView.apply {
            text = App.appContext.resources.getString(R.string.dash_sign_btw_two_text,
                formattedDepDateTime, formattedArrDateTime)
        }
    }

    @BindingAdapter("trainNumber", "isTalgo")
    @JvmStatic
    fun setTrainInfo(textView: TextView, trainNumber: String, isTalgo: Int){
        val isTalgoTrain = if(isTalgo == 1) "«Тальго»" else ""
        textView.apply {
            text = App.appContext.resources.getString(R.string.train_info, trainNumber, isTalgoTrain)
        }
    }

    @BindingAdapter("carNumber", "carTypeLabel", "carClass", "placeNumber")
    @JvmStatic
    fun setCarPlaceInfo(textView: TextView, carNumber: String, carTypeLabel: String,
            carClass: String, places: List<Place>){
        val commaSeparatedPlaceNumber = places.joinToString{ it -> "${it.number}" }
        textView.apply {
            text = App.appContext.resources.getString(R.string.car_place_info, carNumber, carTypeLabel,
                carClass, commaSeparatedPlaceNumber)
        }
    }
}