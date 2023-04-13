package kz.divtech.odyssey.rotation.ui.trips.refund.application

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DEFAULT_PATTERN

object RefundAppListBindingAdapter {

    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    @BindingAdapter("refundStatus")
    @JvmStatic
    fun setViewGroupBackgroundColorByStatus(layout: ConstraintLayout, status: String){
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 12.dpToPx.toFloat()

        shape.apply {
            when(status){
                Constants.REFUND_STATUS_PENDING ->
                    setColor(getColor(R.color.refund_status_pending_bg))
                Constants.REFUND_STATUS_COMPLETED, Constants.REFUND_STATUS_PROCESS ->
                    setColor(getColor(R.color.refund_status_completed_process_bg))
                Constants.REFUND_STATUS_REJECTED, Constants.REFUND_STATUS_CANCELED ->
                    setColor(getColor(R.color.refund_status_rejected_canceled_bg))
                Constants.REFUND_STATUS_ERROR, Constants.REFUND_STATUS_PARTLY ->
                    setColor(getColor(R.color.refund_status_error_partly_bg))
            }
        }
        layout.background = shape
    }

    @BindingAdapter("refundStatus")
    @JvmStatic
    fun setDescByStatus(imageView: ImageView, status: String){
        imageView.apply {
            when (status) {
                Constants.REFUND_STATUS_PENDING -> setImageResource(R.drawable.icon_refund_pending)
                Constants.REFUND_STATUS_PROCESS, Constants.REFUND_STATUS_COMPLETED ->
                    setImageResource(R.drawable.icon_refund_completed)
                Constants.REFUND_STATUS_REJECTED -> setImageResource(R.drawable.icon_refund_rejected)
                Constants.REFUND_STATUS_CANCELED -> setImageResource(R.drawable.icon_refund_cancelled)
                Constants.REFUND_STATUS_PARTLY, Constants.REFUND_STATUS_ERROR ->
                    setImageResource(R.drawable.icon_refund_partly_error)
            }
        }
    }

    @BindingAdapter("refundStatus")
    @JvmStatic
    fun setTitleByStatus(textView: TextView, status: String){
        textView.apply {
            when (status) {
                Constants.REFUND_STATUS_PENDING -> {
                    text = getStringRes(R.string.refund_status_pending_title)
                    setTextColor(getColor(R.color.refund_status_pending_text))
                }
                Constants.REFUND_STATUS_PROCESS -> {
                    text = getStringRes(R.string.refund_status_process_title)
                    setTextColor(getColor(R.color.refund_status_completed_process_text))
                }
                Constants.REFUND_STATUS_COMPLETED -> {
                    text = getStringRes(R.string.refund_status_completed_title)
                    setTextColor(getColor(R.color.refund_status_completed_process_text))
                }
                Constants.REFUND_STATUS_REJECTED -> {
                    text = getStringRes(R.string.refund_status_rejected_title)
                    setTextColor(getColor(R.color.refund_status_rejected_canceled_text))
                }
                Constants.REFUND_STATUS_CANCELED -> {
                    text = getStringRes(R.string.refund_status_canceled_title)
                    setTextColor(getColor(R.color.refund_status_rejected_canceled_text))
                }
                Constants.REFUND_STATUS_ERROR, Constants.REFUND_STATUS_PARTLY -> {
                    text = App.appContext.getString(R.string.refund_status_error_partly_title)
                    setTextColor(getColor(R.color.white))
                }
            }
        }
    }

    @BindingAdapter("refundApp")
    @JvmStatic
    fun setDescByStatus(textView: TextView, refundApp: RefundAppItem){
        val formattedDate = LocalDateTimeUtils.formatByGivenPattern(refundApp.updated_at,
            DEFAULT_PATTERN)
        textView.apply {
            when (refundApp.status) {
                Constants.REFUND_STATUS_PENDING -> {
                    text = getStringRes(R.string.refund_status_pending_desс)
                    setTextColor(getColor(R.color.refund_status_pending_text))
                }
                Constants.REFUND_STATUS_PROCESS -> {
                    text = App.appContext.getString(R.string.refund_status_process_desc, formattedDate)
                    setTextColor(getColor(R.color.refund_status_completed_process_text))
                }
                Constants.REFUND_STATUS_COMPLETED -> {
                    text = App.appContext.getString(R.string.refund_status_completed_desc, formattedDate)
                    setTextColor(getColor(R.color.refund_status_completed_process_text))
                }
                Constants.REFUND_STATUS_REJECTED -> {
                    text = App.appContext.getString(R.string.refund_status_rejected_desc, refundApp.reject_reason)
                    setTextColor(getColor(R.color.refund_status_rejected_canceled_text))
                }
                Constants.REFUND_STATUS_CANCELED -> {
                    text = App.appContext.getString(R.string.refund_status_canceled_desc, formattedDate)
                    setTextColor(getColor(R.color.refund_status_rejected_canceled_text))
                }
                Constants.REFUND_STATUS_ERROR -> {
                    text = App.appContext.getString(R.string.refund_status_error_desc)
                    setTextColor(getColor(R.color.white))
                }
                Constants.REFUND_STATUS_PARTLY -> {
                    val strBuilder = StringBuilder()
                    for(position in 0 until refundApp.segments.size){
                        if(refundApp.segments[position].status == Constants.REFUND_STATUS_ERROR){
                            strBuilder.append(" ${refundApp.realSegment!![position].train?.dep_station_name} - ${refundApp.realSegment!![position].train?.arr_station_name}.")
                        }
                    }
                    text = App.appContext.getString(R.string.refund_status_partly_desc, strBuilder)
                    setTextColor(getColor(R.color.white))
                }
            }
        }
    }

    @BindingAdapter("cancelRefund")
    @JvmStatic fun setCancelRefundVisibility(textView: TextView, status: String){
        textView.isVisible = status == Constants.REFUND_STATUS_PENDING
    }

    @BindingAdapter("refundAppCreatedDateTime")
    @JvmStatic fun setApplicationCreatedDateTime(textView: TextView, dateTime: String){
        val formattedDateTime = LocalDateTimeUtils.formatByGivenPattern(dateTime, DEFAULT_PATTERN)
        textView.text = formattedDateTime
    }


    private fun getColor(colorId: Int) = App.appContext.getColor(colorId)

    private fun getStringRes(stringId: Int) = App.appContext.getString(stringId)
}