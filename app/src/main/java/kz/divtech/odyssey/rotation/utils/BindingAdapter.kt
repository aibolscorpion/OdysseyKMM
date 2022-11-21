package kz.divtech.odyssey.rotation.utils

import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bumptech.glide.Glide
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.ApplicationStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BindingAdapter {

    @BindingAdapter("direction")
    @JvmStatic fun setDirection(textView: TextView, direction: String?) {
        val context = App.appContext
        when (direction) {
            Constants.TO_WORK -> textView.text = context.getString(R.string.to_work)
            Constants.TO_HOME -> textView.text = context.getString(R.string.to_home)
        }
    }

    @BindingConversion
    @JvmStatic fun booleanToVisibility(boolean: Boolean) : Int{
        return if(boolean) View.VISIBLE else View.GONE
    }

    @BindingAdapter("loadFromUrl")
    @JvmStatic fun ImageView.setSrc(imgUrl: String?){
        Glide.with(this.context).load(imgUrl).into(this)
    }

    @BindingAdapter("shift")
    @JvmStatic fun setImageByShift(imageView: ImageView, shift: String?){
        if(shift == Constants.DAY){
            imageView.setImageResource(R.drawable.icons_tabs_shift_day)
        }else if(shift == Constants.NIGHT){
            imageView.setImageResource(R.drawable.icons_tabs_shift_night)
        }
    }


    @BindingAdapter("segmentStatus", "depStationName", "arrStationName")
    @JvmStatic fun depArrStationNames(textView: TextView, segmentStatus: String, depStationName: String, arrStationName: String){
        val depArrStationNames = App.appContext.getString(R.string.dep_arrival_station_name,
            properCase(depStationName), properCase(arrStationName))

        setTextViewBySegmentStatus(textView, segmentStatus)
        textView.text = depArrStationNames
    }



    @BindingAdapter("segmentStatus", "arrDateTime", "depDateTime")
    @JvmStatic fun formatTime(textView: TextView, segmentStatus: String, depDateTime: String?, arrDateTime: String?){
        val serverDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val appDepDateFormat = DateTimeFormatter.ofPattern("dd MMM")
        val appDepTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
        val formattedDepLocalDateTime = LocalDateTime.parse(depDateTime , serverDateTimeFormat)
        val formattedDepDate = formattedDepLocalDateTime.format(appDepDateFormat)
        val formattedDepTime = formattedDepLocalDateTime.format(appDepTimeFormat)

        val formattedArrLocalDateTime = LocalDateTime.parse(arrDateTime , serverDateTimeFormat)
        val formattedArrString = formattedArrLocalDateTime.format(appDepTimeFormat)

        val text = App.appContext.resources.
            getString(R.string.dep_arrival_time, formattedDepDate, formattedDepTime, formattedArrString)
        val styledText: Spanned? = getSpannedText(text)
        setTextViewBySegmentStatus(textView, segmentStatus)
        textView.text = styledText
    }

    @BindingAdapter("applicationStatusIcon", "trip")
    @JvmStatic fun setApplicationStatusIcon(imageView: ImageView, status: ApplicationStatus, trip: Trip){
        var statusIconDrawable = 0
            when(status){
            ApplicationStatus.OPENED_WITHOUT_DETAILS -> statusIconDrawable = R.drawable.icons_tabs_opened_without_details
            ApplicationStatus.OPENED_WITH_DETAILS -> statusIconDrawable = R.drawable.drawable_trip_status_opened_with_details
            ApplicationStatus.OPENED_ON_THE_WAITING_LIST -> statusIconDrawable = R.drawable.drawable_trip_status_opened_on_the_waiting_list
            ApplicationStatus.OPENED_WITH_DETAILS_AND_OPENED_ON_THE_WAITING_LIST -> {
                var drawableId = 0
                trip.segments?.forEachIndexed{ index, segment ->
                    drawableId = if(index == 0) R.id.leftDrawable else R.id.rightDrawable
                    val layerDrawable = ContextCompat.getDrawable(App.appContext,
                        R.drawable.drawable_trip_status_opened_on_the_waiting_list) as LayerDrawable
                    val drawable = layerDrawable.findDrawableByLayerId(drawableId) as GradientDrawable

                    if(segment.status.equals(Constants.STATUS_OPENED))
                        if(segment.active_process.equals(Constants.WATCHING)){
                        drawable.color = ContextCompat.getColorStateList(App.appContext, R.color.on_the_waiting_list_bg)
                    }else if(segment.active_process == null){
                        drawable.color = ContextCompat.getColorStateList(App.appContext, R.color.opened)
                    }
                }
                statusIconDrawable =  drawableId
            }
//            ApplicationStatus.PARTLY_ISSUED_AND_OPENED ->
//            ApplicationStatus.PARTLY_ISSUED_AND_OPENED_ON_THE_WAITING_LIST ->
            ApplicationStatus.RETURNED_FULLY -> statusIconDrawable = R.drawable.drawable_trip_status_returned_fully
//            ApplicationStatus.RETURNED_PARTLY ->
            ApplicationStatus.ISSUED -> statusIconDrawable = R.drawable.drawable_trip_status_issued
            else ->  statusIconDrawable = R.drawable.drawable_trip_status_issued
        }
        imageView.setImageResource(statusIconDrawable)
    }

    private fun setTextViewBySegmentStatus(textView: TextView, segmentStatus: String){
        when(segmentStatus){
            Constants.APP_STATUS_RETURNED -> {
                textView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
            Constants.STATUS_OPENED,
            Constants.APP_STATUS_ON_THE_WAITING_LIST -> {
                textView.setTextColor(ContextCompat.getColor(App.appContext,R.color.grey_text_view))
            }
        }
    }

    private fun getSpannedText(text: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }
    private fun properCase(inputVal: String): String {
        return when (inputVal.length) {
            0 -> ""
            1 -> inputVal.uppercase()
            else -> inputVal.substring(0, 1).uppercase() + inputVal.substring(1).lowercase()
        }
    }

}