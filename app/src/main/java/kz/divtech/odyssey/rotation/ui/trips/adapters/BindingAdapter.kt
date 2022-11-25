package kz.divtech.odyssey.rotation.ui.trips.adapters

import android.graphics.Paint
import android.graphics.drawable.Drawable
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
import kz.divtech.odyssey.rotation.ui.trips.SegmentStatus
import kz.divtech.odyssey.rotation.utils.Utils
import timber.log.Timber
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

    @BindingAdapter("shift")
    @JvmStatic fun setImageByShift(imageView: ImageView, shift: String?){
        if(shift == Constants.DAY){
            imageView.setImageResource(R.drawable.icons_tabs_shift_day)
        }else if(shift == Constants.NIGHT){
            imageView.setImageResource(R.drawable.icons_tabs_shift_night)
        }
    }

    @BindingAdapter("trip")
    @JvmStatic fun setApplicationStatusIcon(imageView: ImageView, trip: Trip){
        when(trip.status){

            Constants.STATUS_OPENED -> {
                if(trip.segments == null){
                    imageView.setImageResource(R.drawable.icons_tabs_opened_without_details)
                }else if(trip.segments.size == 1){
                    if(trip.segments[0].status.equals(Constants.STATUS_OPENED) &&
                        trip.segments[0].active_process.equals(Constants.WATCHING)){
                        imageView.setImageResource(R.drawable.drawable_trip_status_opened_on_the_waiting_list)
                    }else{
                        imageView.setImageResource(R.drawable.drawable_trip_status_opened_with_details)
                    }
                }else {
                    val drawable = moreThanOneSegments(trip)
                    imageView.setImageDrawable(drawable)
                }
            }

            Constants.STATUS_PARTLY -> {
                val drawable = moreThanOneSegments(trip)
                imageView.setImageDrawable(drawable)
            }

            Constants.STATUS_RETURNED -> {
                if(trip.segments?.size  == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_returned_fully)
                }else{
                    val drawable = moreThanOneSegments(trip)
                    imageView.setImageDrawable(drawable)
                }
            }

            Constants.STATUS_ISSUED -> {
                if(trip.segments?.size  == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_issued)
                }else {
                    imageView.setImageResource(R.drawable.drawable_trip_status_issued_more_than_two_segments)
                }
            }
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


    @BindingAdapter("segmentStatus", "depStationName", "arrStationName")
    @JvmStatic fun depArrStationNames(textView: TextView, segmentStatus: String, depStationName: String, arrStationName: String){
        val depArrStationNames = App.appContext.getString(R.string.dep_arrival_station_name,
            properCase(depStationName), properCase(arrStationName)
        )

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

    @BindingAdapter("tripDescIcon")
    @JvmStatic fun setTripDescriptionIcon(imageView: ImageView, trip: Trip){
        trip.segments?.forEachIndexed { _, segment ->
            when(segment.status){
                Constants.STATUS_OPENED ->
                    if(segment.active_process.equals(Constants.WATCHING)){
                        imageView.setImageResource(R.drawable.icon_tabs_on_waiting_list)
                    }
                Constants.STATUS_RETURNED -> imageView.setImageResource(R.drawable.icon_canceled_16px_gray)
                else -> imageView.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("trip")
    @JvmStatic fun setDescription(textView: TextView, trip: Trip){
        when(trip.status){

            Constants.STATUS_OPENED -> {
                if(trip.segments == null){
                    textView.text = App.appContext.getString(R.string.opened_without_details_desc)
                }else if(trip.segments.size == 1){
                    if(trip.segments[0].status.equals(Constants.STATUS_OPENED) &&
                        trip.segments[0].active_process.equals(Constants.WATCHING)){
                        textView.text = App.appContext.getString(R.string.opened_on_the_waiting_list_desc,
                            Utils.formatDateTime(trip.segments[0].watcher_time_limit)
                        )
                    }else{
                        textView.text = App.appContext.getString(R.string.opened_with_details_desc)
                    }
                }else {
                    textView.text = composeTextForMoreThanOneSeg(trip)
                }
            }

            Constants.STATUS_PARTLY -> {
                textView.text = composeTextForMoreThanOneSeg(trip)
            }

            Constants.STATUS_RETURNED -> {
                if(trip.segments?.size  == 1){
                    textView.text = App.appContext.getString(R.string.returned_fully_desc,
                        trip.segments[0].closed_reason,
                        Utils.formatDateTime(trip.segments[0].ticket?.returned_at)
                    )
                }else{
                    textView.text = composeTextForMoreThanOneSeg(trip)
                }
            }

        }
    }

    private fun composeTextForMoreThanOneSeg(trip: Trip): String{
        val strBuilder = StringBuilder()
        val statusSet = HashSet<SegmentStatus>()
        if(trip.segments?.size!! > 1){
            trip.segments.forEachIndexed { index, segment ->
                when(segment.status){
                    Constants.STATUS_OPENED -> {
                        if(segment.active_process.equals(Constants.WATCHING)){
                            statusSet.add(SegmentStatus.ON_THE_WAITING_LIST)
                            strBuilder.append(App.appContext.getString(R.string.on_the_waiting_list_more_than_one_segment,
                                Utils.formatDateTime(segment.watcher_time_limit)
                            ))
                        }else{
                            statusSet.add(SegmentStatus.OPENED)
                            strBuilder.append(App.appContext.getString(R.string.opened_more_than_one_segment))
                        }
                        addNextLine(strBuilder, index, trip.segments.size)
                    }

                    Constants.STATUS_RETURNED -> {
                        statusSet.add(SegmentStatus.RETURNED)
                        strBuilder.append(App.appContext.getString(R.string.returned_more_than_one_segment,
                            segment.closed_reason, Utils.formatDateTime(segment.ticket?.returned_at)
                        ))
                        addNextLine(strBuilder, index, trip.segments.size)
                    }

                    Constants.STATUS_ISSUED -> {
                        statusSet.add(SegmentStatus.ISSUED)
                        strBuilder.append(App.appContext.getString(R.string.issued_more_than_one_segment))
                        addNextLine(strBuilder, index, trip.segments.size)
                    }
                }
            }
            if(statusSet.size == 1){

                if(statusSet.contains(SegmentStatus.OPENED)) {
                    return App.appContext.getString(R.string.opened_with_details_desc)
                }else if(statusSet.contains(SegmentStatus.ON_THE_WAITING_LIST )) {
                    return App.appContext.getString(
                    R.string.opened_on_the_waiting_list_desc,
                        Utils.formatDateTime(trip.segments[0].watcher_time_limit)
                )
                }else if(statusSet.contains(SegmentStatus.RETURNED)){
                    return App.appContext.getString(
                        R.string.returned_fully_desc,
                        trip.segments[0].closed_reason,
                        Utils.formatDateTime(trip.segments[0].ticket?.returned_at)
                    )
                }
            }else if(statusSet.size == 2){
                if(statusSet.contains(SegmentStatus.OPENED) && statusSet.contains(SegmentStatus.ISSUED)){
                    return App.appContext.getString(R.string.issued_partly)
                }
            }
        }
        return strBuilder.toString()
    }

    private fun addNextLine(strBuilder: StringBuilder,index: Int, segmentSize: Int){
        if(index != segmentSize-1) strBuilder.append("\n")
    }

    private fun moreThanOneSegments(trip: Trip) : Drawable? {
        Timber.i("moreThanOneSegments")
        when (trip.segments?.size) {
            2 -> {
                val layerDrawable = ContextCompat.getDrawable(App.appContext,
                    R.drawable.drawable_two_segments) as LayerDrawable

                Timber.e(layerDrawable.toString())
                trip.segments.forEachIndexed { index, segment ->
                    val iconBg = if (index == 0) R.id.leftIconBg else R.id.rightIconBg
                    val icon = if(index == 0) R.id.leftIcon else R.id.rightIcon
                    val iconBgLayerListItem =
                        layerDrawable.findDrawableByLayerId(iconBg) as GradientDrawable

                    when(segment.status){
                        Constants.STATUS_OPENED -> {
                            if(segment.active_process.equals(Constants.WATCHING)){
                                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.on_the_waiting_list_bg))
                            }else{
                                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.opened_bg))
                            }
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
                        }
                        Constants.STATUS_ISSUED -> {
                            iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.issued_bg))
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
                        }
                        Constants.STATUS_RETURNED -> {
                            iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext,R.color.returned_bg))
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_returned))
                        }
                    }
                }
                return layerDrawable

            }
            3 -> {
                val layerDrawable = ContextCompat.getDrawable(App.appContext,
                    R.drawable.drawable_three_segments) as LayerDrawable
                trip.segments.forEachIndexed { index, segment ->
                    val iconBg = when(index) {
                        0 -> R.id.firstIconBg 1 -> R.id.secondIconBg else -> R.id.thirdIconBg
                    }
                    val icon = when(index){
                        0 -> R.id.firstIcon 1 -> R.id.secondIcon else -> R.id.thirdIcon
                    }
                    val iconBgLayerListItem =
                        layerDrawable.findDrawableByLayerId(iconBg) as GradientDrawable

                    when(segment.status){
                        Constants.STATUS_OPENED -> {
                            if(segment.active_process.equals(Constants.WATCHING)){
                                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.on_the_waiting_list_bg))
                            }else{
                                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.opened_bg))
                            }
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
                        }
                        Constants.STATUS_ISSUED -> {
                            iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.issued_bg))
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
                        }
                        Constants.STATUS_RETURNED -> {
                            iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext,R.color.returned_bg))
                            layerDrawable.setDrawableByLayerId(icon,
                                ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_returned))
                        }
                    }
                }
                return layerDrawable
            }
            else -> {
                return null
            }
        }
    }

    private fun setTextViewBySegmentStatus(textView: TextView, segmentStatus: String){
        when(segmentStatus){
            Constants.STATUS_RETURNED -> {
                textView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(App.appContext, R.color.grey_text_view))
                }
            }
            Constants.STATUS_OPENED -> {
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