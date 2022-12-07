package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
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
import kz.divtech.odyssey.rotation.domain.model.trips.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.SegmentStatus
import kz.divtech.odyssey.rotation.utils.Utils

object BindingAdapter {

    @BindingAdapter("direction","date")
    @JvmStatic fun setDirection(textView: TextView, direction: String?, date: String?) {
        val context = App.appContext
        val dayMonth = Utils.formatByGivenPattern(date, Utils.DAY_MONTH_PATTERN)
        textView.text = when (direction) {
            Constants.TO_WORK -> context.getString(R.string.to_work, dayMonth)
            Constants.TO_HOME -> context.getString(R.string.to_home, dayMonth)
            else -> context.getString(R.string.trip, dayMonth)
        }
    }

    @BindingAdapter("shift")
    @JvmStatic fun setImageByShift(imageView: ImageView, shift: String?){
        when (shift) {
            Constants.DAY -> {
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.icons_tabs_shift_day)
            }
            Constants.NIGHT -> {
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.icons_tabs_shift_night)
            }
            else -> {
                imageView.visibility = View.GONE
            }
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
                    imageView.setImageDrawable(moreThanOneSegments(trip))
                }
            }

            Constants.STATUS_PARTLY -> {
                imageView.setImageDrawable(moreThanOneSegments(trip))
            }

            Constants.STATUS_RETURNED -> {
                if(trip.segments?.size == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_returned_fully)
                }else{
                    imageView.setImageDrawable(moreThanOneSegments(trip))
                }
            }

            Constants.STATUS_ISSUED -> {
                if(trip.segments?.size  == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_issued)
                }else {
                    imageView.setImageDrawable(moreThanOneSegments(trip))}
            }
        }
    }

    private fun moreThanOneSegments(trip: Trip) : Drawable? {
        val statusSet = HashSet<SegmentStatus>()
        var layerDrawable: LayerDrawable? = null
        when (trip.segments?.size) {
            2 -> {
                layerDrawable = ContextCompat.getDrawable(App.appContext,
                    R.drawable.drawable_two_segments) as LayerDrawable

                trip.segments.forEachIndexed { index, segment ->
                    val iconBg = if (index == 0) R.id.leftIconBg else R.id.rightIconBg
                    val icon = if(index == 0) R.id.leftIcon else R.id.rightIcon
                    val iconBgLayerListItem =
                        layerDrawable!!.findDrawableByLayerId(iconBg) as GradientDrawable

                    generateIconForEachSegment(segment, statusSet,
                        layerDrawable!!, icon, iconBgLayerListItem)

                }

            }
            3 -> {
                layerDrawable = ContextCompat.getDrawable(App.appContext,
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

                    generateIconForEachSegment(segment, statusSet, layerDrawable, icon, iconBgLayerListItem)
                }

            }
        }
        return if(statusSet.size == 1){
            var drawableId: Int? = null
            if(statusSet.contains(SegmentStatus.OPENED)){
                drawableId = R.drawable.drawable_trip_status_opened_more_than_two_segments
            }else if(statusSet.contains(SegmentStatus.ON_THE_WAITING_LIST)){
                drawableId = R.drawable.drawable_trip_status_on_the_waiting_list_more_than_two_segments
            }else if(statusSet.contains(SegmentStatus.RETURNED)){
                drawableId = R.drawable.drawable_trip_status_returned_fully
            }else if(statusSet.contains(SegmentStatus.ISSUED)){
                drawableId = R.drawable.drawable_trip_status_issued_more_than_two_segments
            }
            ContextCompat.getDrawable(App.appContext, drawableId!!)
        }else{
            layerDrawable
        }
    }

    private fun generateIconForEachSegment(segment: Segment, statusSet: HashSet<SegmentStatus>,
                                           layerDrawable: LayerDrawable, icon: Int, iconBgLayerListItem: GradientDrawable){
        when(segment.status){
            Constants.STATUS_OPENED -> {
                if(segment.active_process.equals(Constants.WATCHING)){
                    statusSet.add(SegmentStatus.ON_THE_WAITING_LIST)
                    iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.on_the_waiting_list_bg))
                }else{
                    statusSet.add(SegmentStatus.OPENED)
                    iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.opened_bg))
                }
                layerDrawable.setDrawableByLayerId(icon,
                    ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
            }
            Constants.STATUS_ISSUED -> {
                statusSet.add(SegmentStatus.ISSUED)
                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext, R.color.issued_bg))
                layerDrawable.setDrawableByLayerId(icon,
                    ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_train))
            }
            Constants.STATUS_RETURNED -> {
                statusSet.add(SegmentStatus.RETURNED)
                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext,R.color.returned_bg))
                layerDrawable.setDrawableByLayerId(icon,
                    ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_returned))
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

        setPaintFlagsAndColorBySegmentStatus(textView, segmentStatus)
        textView.text = depArrStationNames
    }

    @BindingAdapter("segmentStatus", "depDateTime",  "arrDateTime")
    @JvmStatic fun formatTime(textView: TextView, segmentStatus: String, depDateTime: String?, arrDateTime: String?){
        val formattedDepDate = Utils.formatByGivenPattern(depDateTime,Utils.DAY_MONTH_PATTERN)
        val formattedDepTime = Utils.formatByGivenPattern(depDateTime, Utils.HOUR_MINUTE_PATTERN)
        val formattedArrTime = Utils.formatByGivenPattern(arrDateTime,  Utils.HOUR_MINUTE_PATTERN)

        val text = App.appContext.resources.
            getString(R.string.dep_arrival_time, formattedDepDate, formattedDepTime, formattedArrTime)
        setPaintFlagsAndColorBySegmentStatus(textView, segmentStatus)
        setSpannedText(textView, text)
    }

    private fun setPaintFlagsAndColorBySegmentStatus(textView: TextView, segmentStatus: String){

        textView.apply {
            when(segmentStatus){
                Constants.STATUS_RETURNED -> {
                    setTextColor(ContextCompat.getColor(App.appContext, R.color.grey_text_view))
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                Constants.STATUS_OPENED -> {
                    setTextColor(ContextCompat.getColor(App.appContext, R.color.grey_text_view))
                    paintFlags = 0
                    }

                Constants.STATUS_ISSUED -> {
                    setTextColor(ContextCompat.getColor(App.appContext, R.color.profile_menu_text))
                    paintFlags = 0
                }
            }
        }
    }

    @BindingAdapter("htmlText")
    @JvmStatic fun setSpannedText(textView: TextView, text: String?){
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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

    @BindingAdapter("descVisibility")
    @JvmStatic fun setDescVisibility(viewGroup: ViewGroup, trip: Trip){
        val statusSet = HashSet<SegmentStatus>()
        if(trip.status.equals(Constants.STATUS_ISSUED)){
            trip.segments?.forEach { segment ->
                when(segment.status){
                    Constants.STATUS_ISSUED -> statusSet.add(SegmentStatus.ISSUED)
                    Constants.STATUS_RETURNED -> statusSet.add(SegmentStatus.RETURNED)
                }
            }
        }
        viewGroup.visibility = if(statusSet.size == 1 && statusSet.contains(SegmentStatus.ISSUED)
            && !trip.is_extra!!)
                View.GONE else View.VISIBLE
    }

    @BindingAdapter("tripDescIcon")
    @JvmStatic fun setTripDescriptionIcon(imageView: ImageView, trip: Trip){
        val statusSet = HashSet<SegmentStatus>()

        statusSet.apply {
            trip.segments?.forEachIndexed { _, segment ->
                when(segment.status){
                    Constants.STATUS_OPENED ->
                        if(segment.active_process.equals(Constants.WATCHING)){
                            add(SegmentStatus.ON_THE_WAITING_LIST)
                        }
                    Constants.STATUS_RETURNED -> {
                        add(SegmentStatus.RETURNED)
                    }
                }
            }
        }

        imageView.apply {
            if(statusSet.contains(SegmentStatus.RETURNED)){
                visibility = View.VISIBLE
                setImageResource(R.drawable.icon_canceled_16px_gray)
            }else if(statusSet.contains(SegmentStatus.ON_THE_WAITING_LIST)){
                visibility = View.VISIBLE
                setImageResource(R.drawable.icon_tabs_on_waiting_list)
            }else{
                visibility = View.GONE
            }
        }

    }

    @BindingAdapter("tripDescriptionTitle")
    @JvmStatic fun setDescriptionTitle(textView: TextView, trip: Trip){
        val context = textView.context
        val title = when(trip.status){
            Constants.STATUS_OPENED -> context.getString(R.string.status_opened_description_title)
            Constants.STATUS_RETURNED -> context.getString(R.string.status_returned_description_title)
            Constants.STATUS_PARTLY -> context.getString(R.string.status_partly_description_title)
            Constants.STATUS_ISSUED -> context.getString(R.string.status_returned_partly_title)
            else -> ""
        }
        textView.text = title
    }

    @BindingAdapter("tripDescription")
    @JvmStatic fun setDescription(textView: TextView, trip: Trip){
        val strBuilder = StringBuilder()
        if(trip.is_extra!!){
            strBuilder.append(App.appContext.getString(R.string.extra_application))
        }
        when(trip.status){
            Constants.STATUS_OPENED -> {
                if(trip.segments == null){
                    strBuilder.append(App.appContext.getString(R.string.opened_without_details_desc))
                }else if(trip.segments.size == 1){
                    if(trip.segments[0].status.equals(Constants.STATUS_OPENED) &&
                        trip.segments[0].active_process.equals(Constants.WATCHING)){
                        strBuilder.append(App.appContext.getString(R.string.opened_on_the_waiting_list_desc,
                            Utils.formatByGivenPattern(trip.segments[0].watcher_time_limit, Utils.DEFAULT_PATTERN))
                        )
                    }else{
                        strBuilder.append(App.appContext.getString(R.string.opened_with_details_desc))
                    }
                }else {
                    strBuilder.append(composeTextForMoreThanOneSeg(trip))
                }
            }

            Constants.STATUS_PARTLY -> {
                strBuilder.append(composeTextForMoreThanOneSeg(trip))
            }

            Constants.STATUS_RETURNED -> {
                if(trip.segments?.size  == 1){
                    strBuilder.append(App.appContext.getString(R.string.returned_fully_desc,
                        trip.segments[0].closed_reason,
                        Utils.formatByGivenPattern(trip.segments[0].ticket?.returned_at, Utils.DEFAULT_PATTERN)
                    ))
                }else{
                    strBuilder.append(composeTextForMoreThanOneSeg(trip))
                }
            }

            Constants.STATUS_ISSUED -> {
                if(trip.segments?.size!! > 1){
                    strBuilder.append(composeTextForMoreThanOneSeg(trip))
                }
            }
        }
        textView.text = strBuilder.toString()
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
                                Utils.formatByGivenPattern(segment.watcher_time_limit, Utils.DEFAULT_PATTERN)
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
                            segment.closed_reason, Utils.formatByGivenPattern(segment.ticket?.returned_at, Utils.DEFAULT_PATTERN)
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
                        Utils.formatByGivenPattern(trip.segments[0].watcher_time_limit, Utils.DEFAULT_PATTERN)
                )
                }else if(statusSet.contains(SegmentStatus.RETURNED)){
                    return App.appContext.getString(
                        R.string.returned_fully_desc,
                        trip.segments[0].closed_reason,
                        Utils.formatByGivenPattern(trip.segments[0].ticket?.returned_at, Utils.DEFAULT_PATTERN)
                    )
                }else if(statusSet.contains(SegmentStatus.ISSUED)){
                    return ""
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


    @BindingAdapter("depArrDate")
    @JvmStatic fun setDepArrDate(textView: TextView, dateTime: String){
        textView.text = Utils.formatByGivenPattern(dateTime, Utils.DAY_MONTH_DAY_OF_WEEK_PATTERN)
    }

    @BindingAdapter("depArrTime")
    @JvmStatic fun setDepArrTime(textView: TextView, dateTime: String){
        textView.text = Utils.formatByGivenPattern(dateTime, Utils.HOUR_MINUTE_PATTERN)
    }


}