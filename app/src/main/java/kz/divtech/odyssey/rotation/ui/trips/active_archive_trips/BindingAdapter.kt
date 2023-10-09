package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.model.trips.SegmentStatus
import kz.divtech.odyssey.rotation.ui.trips.refund.application.RefundAppListBindingAdapter.dpToPx
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.DAY_MONTH_DAY_OF_WEEK_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.DAY_MONTH_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.DEFAULT_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.HOUR_MINUTE_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.formatDateTimeToGivenPattern
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.formatDateToGivenPattern
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.getLocalDateTimeByPattern
import kz.divtech.odyssey.rotation.common.utils.Utils.getAppLocale
import java.time.temporal.ChronoUnit

object BindingAdapter {

    @BindingAdapter("direction","date")
    @JvmStatic fun setDirection(textView: TextView, direction: String?, date: String?) {
        val context = textView.context
        val dayMonth = date.formatDateToGivenPattern(DAY_MONTH_PATTERN, context.getAppLocale())
        textView.text = when (direction) {
            Constants.TO_WORK -> context.getString(R.string.to_work, dayMonth)
            Constants.TO_HOME -> context.getString(R.string.to_home, dayMonth)
            else -> context.getString(R.string.trip, dayMonth)
        }
    }

    @BindingAdapter("shift")
    @JvmStatic fun setImageByShift(imageView: ImageView, shift: String?){
        imageView.isVisible = shift == Constants.NIGHT
    }

    @BindingAdapter("trip")
    @JvmStatic fun setApplicationStatusIcon(imageView: ImageView, trip: Trip?){
        when(trip?.status){
            Constants.STATUS_OPENED -> {
                if(trip.segments.isEmpty()){
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
                if(trip.segments.size == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_returned_fully)
                }else{
                    imageView.setImageDrawable(moreThanOneSegments(trip))
                }
            }

            Constants.STATUS_ISSUED -> {
                if(trip.segments.size == 1){
                    imageView.setImageResource(R.drawable.drawable_trip_status_issued)
                }else {
                    imageView.setImageDrawable(moreThanOneSegments(trip))}
            }
        }
    }

    private fun moreThanOneSegments(trip: Trip) : Drawable? {
        val statusSet = HashSet<SegmentStatus>()
        var layerDrawable: LayerDrawable? = null
        when (trip.segments.size) {
            2 -> {
                layerDrawable = ContextCompat.getDrawable(
                    App.appContext,
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
                layerDrawable = ContextCompat.getDrawable(
                    App.appContext,
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
            Constants.STATUS_CANCELED, Constants.STATUS_RETURNED -> {
                statusSet.add(SegmentStatus.RETURNED)
                iconBgLayerListItem.setColor(ContextCompat.getColor(App.appContext,R.color.returned_bg))
                layerDrawable.setDrawableByLayerId(icon,
                    ContextCompat.getDrawable(App.appContext, R.drawable.icons_tabs_returned))
            }
        }
    }

    @BindingAdapter("refundSegmentStatus")
    @JvmStatic fun setSegmentIcon(imageView: ImageView, refundSegmentStatus: String?){
        when(refundSegmentStatus){
            Constants.REFUND_STATUS_PROCESS, Constants.STATUS_RETURNED ->
                imageView.setImageResource(R.drawable.icon_refund_completed)
            Constants.REFUND_STATUS_ERROR -> imageView.setImageResource(R.drawable.icon_refund_partly_error_red)
            Constants.REFUND_STATUS_PENDING -> {
                val drawable = ContextCompat.getDrawable(imageView.context, R.drawable.animated_drawable)
                (drawable as AnimatedVectorDrawable).start()
                imageView.setImageDrawable(drawable)
            }
            Constants.REFUND_STATUS_REJECTED -> imageView.setImageResource(R.drawable.icon_refund_rejected)
            null -> Glide.with(imageView.context).load(R.mipmap.ktzh_logo).into(imageView) }
    }

    @BindingAdapter("segmentStatus", "depStationName", "arrStationName")
    @JvmStatic fun depArrStationNames(textView: TextView, segmentStatus: String?, depStationName: String?, arrStationName: String?){
        if(depStationName != null && arrStationName != null && segmentStatus != null){
            val depArrStationNames = textView.context.getString(R.string.dash_sign_btw_two_text,
                properCase(depStationName), properCase(arrStationName)
            )

            setPaintFlagsAndColorBySegmentStatus(textView, segmentStatus)
            textView.text = depArrStationNames
        }
    }

    @BindingAdapter("segmentStatus", "depDateTime",  "arrDateTime")
    @JvmStatic fun formatTime(textView: TextView, segmentStatus: String?, depDateTime: String?, arrDateTime: String?){
        if(depDateTime != null && arrDateTime != null && segmentStatus != null){
            val formattedDepTime = depDateTime.formatDateTimeToGivenPattern(HOUR_MINUTE_PATTERN)
            val formattedArrTime = arrDateTime.formatDateTimeToGivenPattern(HOUR_MINUTE_PATTERN)

            val text = textView.context.getString(R.string.dash_sign_btw_two_text, formattedDepTime, formattedArrTime)
            setPaintFlagsAndColorBySegmentStatus(textView, segmentStatus)
            textView.text = text
        }
    }

    @BindingAdapter("segmentStatus", "depDateTime")
    @JvmStatic fun formatDate(textView: TextView, segmentStatus: String?, depDateTime: String?){
        if(segmentStatus != null && depDateTime != null){
            val formattedDepDate = depDateTime.formatDateTimeToGivenPattern(DAY_MONTH_DAY_OF_WEEK_PATTERN,
                textView.context.getAppLocale())

            setPaintFlagsAndColorBySegmentStatus(textView, segmentStatus)
            textView.text = formattedDepDate
        }
    }

    private fun setPaintFlagsAndColorBySegmentStatus(textView: TextView, segmentStatus: String){

        textView.apply {
            when(segmentStatus){
                Constants.STATUS_CANCELED, Constants.STATUS_RETURNED -> {
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
        if(!text.isNullOrEmpty()){
            textView.text = Html.fromHtml(text, FROM_HTML_MODE_LEGACY)
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
    @JvmStatic fun setDescVisibility(viewGroup: ViewGroup, trip: Trip?){
        val statusSet = HashSet<SegmentStatus>()
        if(trip?.status.equals(Constants.STATUS_ISSUED)){
            trip?.segments?.forEach { segment ->
                when(segment.status){
                    Constants.STATUS_ISSUED -> statusSet.add(SegmentStatus.ISSUED)
                    Constants.STATUS_RETURNED -> statusSet.add(SegmentStatus.RETURNED)
                    Constants.STATUS_CANCELED -> statusSet.add(SegmentStatus.CANCELED)
                }
            }
        }
        viewGroup.visibility = if(statusSet.size == 1 && statusSet.contains(SegmentStatus.ISSUED))
                View.GONE else View.VISIBLE
    }

    @BindingAdapter("tripDescIcon")
    @JvmStatic fun setTripDescriptionIcon(imageView: ImageView, trip: Trip?){
        val statusSet = HashSet<SegmentStatus>()

        statusSet.apply {
            trip?.segments?.forEachIndexed { _, segment ->
                when(segment.status){
                    Constants.STATUS_OPENED ->
                        if(segment.active_process.equals(Constants.WATCHING)){
                            add(SegmentStatus.ON_THE_WAITING_LIST)
                        }
                    Constants.STATUS_RETURNED -> {
                        add(SegmentStatus.RETURNED)
                    }
                    Constants.STATUS_CANCELED -> {
                        add(SegmentStatus.CANCELED)
                    }
                }
            }
        }

        imageView.apply {
            if(statusSet.contains(SegmentStatus.RETURNED) || statusSet.contains(SegmentStatus.CANCELED)){
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
    @JvmStatic fun setDescriptionTitle(textView: TextView, trip: Trip?){
        val context = textView.context
        val title = when(trip?.status){
            Constants.STATUS_OPENED -> context.getString(R.string.status_opened_description_title)
            Constants.STATUS_RETURNED -> context.getString(R.string.status_returned_description_title)
            Constants.STATUS_PARTLY -> context.getString(R.string.status_partly_description_title)
            Constants.STATUS_ISSUED -> context.getString(R.string.status_returned_partly_title)
            else -> ""
        }
        textView.text = title
    }

    @BindingAdapter("tripDescription")
    @JvmStatic fun setDescription(textView: TextView, trip: Trip?){
        val context = textView.context
        val strBuilder = StringBuilder()
        if (trip != null && trip.is_extra) {
            strBuilder.append(context.getString(R.string.extra_application))
            strBuilder.append(Constants.SPACE)
        }
        when(trip?.status){
            Constants.STATUS_OPENED -> {
                if(trip.segments.isEmpty()){
                    strBuilder.append(context.getString(R.string.opened_without_details_desc))
                }else if(trip.segments.size == 1){
                    if(trip.segments[0].status == Constants.STATUS_OPENED &&
                        trip.segments[0].active_process.equals(Constants.WATCHING)){
                        strBuilder.append(context.getString(R.string.opened_on_the_waiting_list_desc,
                            trip.segments[0].watcher_time_limit.formatDateTimeToGivenPattern(DEFAULT_PATTERN,
                                context.getAppLocale())))
                    }else{
                        strBuilder.append(context.getString(R.string.opened_with_details_desc))
                    }
                }else {
                    strBuilder.append(context.composeTextForMoreThanOneSeg(trip))
                }
            }

            Constants.STATUS_PARTLY -> {
                strBuilder.append(context.composeTextForMoreThanOneSeg(trip))
            }

            Constants.STATUS_RETURNED -> {
                if(trip.segments.size  == 1){
                    strBuilder.append(context.getString(R.string.returned_fully_one_segment_desc,
                        trip.segments[0].closed_reason,
                        trip.segments[0].ticket?.returned_at.formatDateTimeToGivenPattern(DEFAULT_PATTERN,
                            context.getAppLocale())
                    ))
                }else{
                    strBuilder.append(context.composeTextForMoreThanOneSeg(trip))
                }
            }

            Constants.STATUS_ISSUED -> {
                if(trip.segments.size > 1){
                    strBuilder.append(context.composeTextForMoreThanOneSeg(trip))
                }
            }
        }
        textView.text = strBuilder.toString()
    }

    private fun Context.composeTextForMoreThanOneSeg(trip: Trip): String{
        val strBuilder = StringBuilder()
        val statusSet = HashSet<SegmentStatus>()

        if(trip.segments.size > 1){
            trip.segments.forEach{ segment ->
                when(segment.status){
                    Constants.STATUS_OPENED -> {
                        if(segment.active_process.equals(Constants.WATCHING)){
                            statusSet.add(SegmentStatus.ON_THE_WAITING_LIST)
                            strBuilder.append(this.getString(
                                R.string.on_the_waiting_list_more_than_one_segment,
                                segment.watcher_time_limit.formatDateTimeToGivenPattern(DEFAULT_PATTERN,
                                this.getAppLocale())
                            ))
                        }else{
                            statusSet.add(SegmentStatus.OPENED)
                            strBuilder.append(this.getString(R.string.opened_more_than_one_segment))
                        }
                    }

                    Constants.STATUS_ISSUED -> {
                        statusSet.add(SegmentStatus.ISSUED)
                        strBuilder.append(this.getString(R.string.issued_more_than_one_segment))
                    }

                    Constants.STATUS_CANCELED, Constants.STATUS_RETURNED -> {
                        statusSet.add(SegmentStatus.RETURNED)
                        strBuilder.append(this.getString(
                            R.string.returned_more_than_one_segment, segment.closed_reason))
                    }
                }
                strBuilder.append(Constants.SPACE)
            }

            if(statusSet.size == 1){
                if(statusSet.contains(SegmentStatus.OPENED)) {
                    return this.getString(R.string.opened_with_details_desc)
                }else if(statusSet.contains(SegmentStatus.ON_THE_WAITING_LIST )) {
                    return this.getString(R.string.opened_on_the_waiting_list_desc,
                        trip.segments[0].watcher_time_limit.formatDateTimeToGivenPattern(DEFAULT_PATTERN,
                        this.getAppLocale()))
                }else if(statusSet.contains(SegmentStatus.RETURNED)){
                    return this.getString(
                        R.string.returned_fully_desc, trip.segments[0].closed_reason)
                }else if(statusSet.contains(SegmentStatus.ISSUED)){
                    return ""
                }
            }else if(statusSet.size == 2){
                if(statusSet.contains(SegmentStatus.OPENED) && statusSet.contains(SegmentStatus.ISSUED)){
                    return this.getString(R.string.issued_partly)
                }
            }
        }
        return strBuilder.toString()
    }

    @BindingAdapter("trip")
    @JvmStatic fun setCityAndTotalTimeInWay(textView: TextView, trip: Trip){
        if(trip.segments.first().train != null && trip.segments.last().train != null){
            val firstSegmentDepLocalDateTime = trip.segments.first().train?.dep_date_time?.getLocalDateTimeByPattern()
            val lastSegmentArrLocalDateTime = trip.segments.last().train?.arr_date_time?.getLocalDateTimeByPattern()
            val totalMinutes = firstSegmentDepLocalDateTime?.until(lastSegmentArrLocalDateTime, ChronoUnit.MINUTES)
            val hours = totalMinutes?.div(60)
            val minutes = totalMinutes?.rem(60)
            val totalTimeInWay = textView.context.getString(R.string.total_time_in_way, trip.end_station?.name, hours, minutes)
            textView.text = totalTimeInWay
        }else{
            trip.end_station?.name?.let {
                textView.text = it
            }
        }
    }

    @BindingAdapter("depArrDate")
    @JvmStatic fun setDepArrDate(textView: TextView, dateTime: String?){
        textView.text = dateTime.formatDateTimeToGivenPattern(DAY_MONTH_DAY_OF_WEEK_PATTERN,
            textView.context.getAppLocale())
    }

    @BindingAdapter("depArrTime")
    @JvmStatic fun setDepArrTime(textView: TextView, dateTime: String?){
        textView.text = dateTime.formatDateTimeToGivenPattern(HOUR_MINUTE_PATTERN)
    }

    @BindingAdapter("refundSegmentStatus")
    @JvmStatic fun setSegmentRefundStatus(textView: TextView, refundSegmentStatus: String?){
        val context = textView.context
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = 6.dpToPx.toFloat()
        when(refundSegmentStatus){
            Constants.REFUND_STATUS_PROCESS -> {
                drawable.setColor(App.appContext.getColor(R.color.refund_status_completed_process_bg))
                textView.apply {
                    setTextColor(App.appContext.getColor(R.color.refund_status_completed_process_text))
                    text = context.getString(R.string.segment_status_process)
                }
            }
            Constants.STATUS_RETURNED -> {
                drawable.setColor(App.appContext.getColor(R.color.refund_status_completed_process_bg))
                textView.apply {
                    setTextColor(App.appContext.getColor(R.color.refund_status_completed_process_text))
                    text = context.getString(R.string.segment_status_returned)
                }
            }
            Constants.REFUND_STATUS_ERROR -> {
                drawable.setColor(App.appContext.getColor(R.color.refund_status_error_partly_bg))
                textView.apply {
                    setTextColor(App.appContext.getColor(R.color.white))
                    text = context.getString(R.string.segment_status_error)
                }

            }
            Constants.REFUND_STATUS_PENDING -> {
                drawable.setColor(App.appContext.getColor(R.color.refund_status_pending_bg))
                textView.apply {
                    setTextColor(App.appContext.getColor(R.color.refund_status_pending_text))
                    text = context.getString(R.string.segment_status_pending)
                }

            }
            Constants.REFUND_STATUS_REJECTED -> {
                drawable.setColor(App.appContext.getColor(R.color.refund_status_rejected_canceled_bg))
                textView.apply {
                    setTextColor(App.appContext.getColor(R.color.refund_status_rejected_canceled_text))
                    text = context.getString(R.string.segment_status_rejected)
                }
            }
            null -> textView.isVisible = false
        }
        textView.background = drawable
    }


}