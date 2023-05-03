package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_HOUR_MINUTE_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatDateTimeToGivenPattern

object RefundBindingAdapter {

    @BindingAdapter("ticketDepDateTime", "ticketArrDateTime")
    @JvmStatic
    fun setTicketDepArrDateTime(textView: TextView, ticketDepDateTime: String?, ticketArrDateTime: String?) {
        val formattedDepDateTime = ticketDepDateTime.formatDateTimeToGivenPattern(DAY_MONTH_HOUR_MINUTE_PATTERN)
        val formattedArrDateTime = ticketArrDateTime.formatDateTimeToGivenPattern(DAY_MONTH_HOUR_MINUTE_PATTERN)

        textView.apply {
            text = App.appContext.resources.getString(R.string.dash_sign_btw_two_text,
                formattedDepDateTime, formattedArrDateTime)
        }
    }

    @BindingAdapter("trainNumber", "isTalgo")
    @JvmStatic
    fun setTrainInfo(textView: TextView, trainNumber: String, isTalgo: Boolean){
        val isTalgoTrain = if(isTalgo) "«Тальго»" else ""
        textView.apply {
            text = App.appContext.resources.getString(R.string.train_info, trainNumber, isTalgoTrain)
        }
    }

    @BindingAdapter("carNumber", "carTypeLabel", "carClass", "placeNumber")
    @JvmStatic
    fun setCarPlaceInfo(textView: TextView, carNumber: String, carTypeLabel: String,
            carClass: String, places: String){
        textView.apply {
            text = App.appContext.resources.getString(R.string.car_place_info, carNumber, carTypeLabel,
                carClass, places)
        }
    }
}