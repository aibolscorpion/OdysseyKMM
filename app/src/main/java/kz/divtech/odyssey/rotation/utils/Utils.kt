package kz.divtech.odyssey.rotation.utils

import android.os.Bundle
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem

object Utils {

    fun StringBuilder.appendWithoutNull(text: String?): StringBuilder{
        if(text != null){
            append(text)
        }
        return this
    }

    fun Bundle.convertToNotification(): PushNotification {
        val id = getString(Constants.NOTIFICATION_DATA_ID)
        val sendTime = getString(Constants.NOTIFICATION_DATA_SEND_TIME)
        val notifiableType = getString(Constants.NOTIFICATION_DATA_NOTIFIABLE_TYPE)
        val title = getString(Constants.NOTIFICATION_DATA_TITLE)
        val content = getString(Constants.NOTIFICATION_DATA_CONTENT)
        val type = getString(Constants.NOTIFICATION_DATA_TYPE)
        val isImportant = getString(Constants.NOTIFICATION_DATA_IS_IMPORTANT)?.toBoolean()
        val applicationId = getString(Constants.NOTIFICATION_DATA_APPLICATION_ID)?.toInt()
        return PushNotification(id!!, notifiableType!!, sendTime!!,
            title!!, content!!, type!!, isImportant!!, applicationId)
    }

    fun getRefundSegmentStatus(refundAppList: List<RefundAppItem>, segmentId: Int): String?{
        if(refundAppList.isNotEmpty()){
            refundAppList.forEach { refundAppItem ->
                refundAppItem.segments.forEach { refundSegment ->
                    if(refundSegment.segment_id == segmentId){
                        return when(refundAppItem.status){
                            Constants.REFUND_STATUS_PENDING -> Constants.REFUND_STATUS_PENDING
                            Constants.REFUND_STATUS_REJECTED -> Constants.REFUND_STATUS_REJECTED
                            else -> refundSegment.status
                        }
                    }
                }
            }
        }
        return null
    }

}