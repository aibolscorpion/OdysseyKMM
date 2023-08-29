package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.HOUR_MINUTE_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatDateTimeToGivenPattern
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.getLocalDateTimeByPattern
import kz.divtech.odyssey.rotation.utils.Utils.getAppLocale
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object NotificationBindingAdapter {

    @BindingAdapter("notificationIcon")
    @JvmStatic fun setNotificationIcon(imageView: ImageView, notificationType: String?){
        val typeArray = notificationType?.split("\\")

        val iconResource: Int = when(typeArray?.get(typeArray.size-1)){
            "TripStarted" -> R.drawable.drawable_trip_status_issued
            "SegmentRemovedFromWaitingList" -> R.drawable.icon_removed_from_waiting_list
            "SegmentAddedToWaitingList" -> R.drawable.icon_added_to_waiting_list
            "SegmentHasPlaces" -> R.drawable.icon_places_found_waiting_list
            "TicketReturned" -> R.drawable.drawable_trip_status_returned_fully
            "SegmentIssued" -> R.drawable.icon_ticket_issued
            "NewEmployeeScheduleShiftRecived" -> R.drawable.icon_schedule_shift
            "DocumentExpiredInfo" -> R.drawable.icon_document_expired
            "NewVacationRecived" -> R.drawable.icon_vacation
            "NewDismissedRecived" -> R.drawable.icon_fired
            "NewOvertimeRecived" -> R.drawable.icon_rvd
            "LoginFromOtherDevice" -> R.drawable.icon_notify_another_device
            "RefundApplicationRejected" -> R.drawable.icon_refund_rejected
            "RefundApplicationConfirmed" -> R.drawable.icon_refund_completed
            "RefundSucceed" -> R.drawable.icon_refund_completed
            "RefundError" -> R.drawable.icon_refund_partly_error_red
            else -> R.drawable.icon_notifications
        }
        imageView.setImageResource(iconResource)
    }


    @BindingAdapter("isImportant")
    @JvmStatic fun setIsImportant(textView: TextView, isImportant: Boolean?){
        val context = textView.context
        textView.text = if(isImportant!!)
            context.getString(R.string.important_notification)
        else
            context.getString(R.string.notification)
    }

    @BindingAdapter("updatedAt")
    @JvmStatic fun setUpdatedAt(textView: TextView, updatedAt: String?){
        val context = textView.context
        val currentLocalDateTime = LocalDateTime.now()
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val updatedLocalDateTime = updatedAt?.getLocalDateTimeByPattern()

        val date = updatedAt.formatDateTimeToGivenPattern(DAY_MONTH_PATTERN, context.getAppLocale())
        val time = updatedAt.formatDateTimeToGivenPattern(HOUR_MINUTE_PATTERN)

        if (updatedLocalDateTime != null) {
            textView.text = when(updatedLocalDateTime.toLocalDate()){
                today -> {
                    when(val minutes = updatedLocalDateTime.until(currentLocalDateTime, ChronoUnit.MINUTES)) {
                        in 0..60 -> context.getString(R.string.minutes_before, minutes)
                        in 60..120 -> context.getString(R.string.hours_before, 1)
                        in 120..180 -> context.getString(R.string.hours_before, 2)
                        else -> time
                    }
                }
                yesterday -> context.getString(R.string.yesterday_at_time, time)
                else -> context.getString(R.string.date_at_time, date, time)
            }
        }
    }

    @BindingAdapter("learnMoreVisibility")
    @JvmStatic fun setLearnMoreVisibility(learnMoreLL: LinearLayout, notificationTypeGroup: String?){
        learnMoreLL.visibility =
            when(notificationTypeGroup){
                Constants.NOTIFICATION_TYPE_TICKET, Constants.NOTIFICATION_TYPE_APPLICATION,
                Constants.NOTIFICATION_TYPE_REFUND_APPLICATION -> View.VISIBLE
                else -> View.GONE
        }
    }

    @BindingAdapter("importantButtonText")
    @JvmStatic fun setImportantButtonText(button: AppCompatButton, isImportant: Boolean?){
        val context = button.context
        button.text = if(isImportant!!)
            context.getString(R.string.important_button_text)
        else
            context.getString(R.string.close)
    }

}