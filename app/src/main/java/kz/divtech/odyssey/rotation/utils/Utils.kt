package kz.divtech.odyssey.rotation.utils

import android.os.Bundle
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification

object Utils {

    fun StringBuilder.appendWithoutNull(text: String?): StringBuilder{
        if(text != null){
            append(text)
        }
        return this
    }

    fun convertBundleToNotification(bundle : Bundle): PushNotification {
        val id = bundle.getString(Constants.NOTIFICATION_DATA_ID)
        val sendTime = bundle.getString(Constants.NOTIFICATION_DATA_SEND_TIME)
        val notifiableType = bundle.getString(Constants.NOTIFICATION_DATA_NOTIFIABLE_TYPE)
        val title = bundle.getString(Constants.NOTIFICATION_DATA_TITLE)
        val content = bundle.getString(Constants.NOTIFICATION_DATA_CONTENT)
        val type = bundle.getString(Constants.NOTIFICATION_DATA_TYPE)
        val isImportant = bundle.getString(Constants.NOTIFICATION_DATA_IS_IMPORTANT)?.toBoolean()
        val applicationId = bundle.getString(Constants.NOTIFICATION_DATA_APPLICATION_ID)?.toInt()
        return PushNotification(id!!, notifiableType!!, sendTime!!,
            title!!, content!!, type!!, isImportant!!, applicationId)
    }

    fun convertNotification(notification: Notification): PushNotification{
        return PushNotification(
            notification.id,
            notification.type,
            notification.created_at,
            notification.data.data!!.title,
            notification.data.data.content,
            notification.data.data.type,
            notification.data.data.is_important,
            notification.data.data.application_id)
    }

}