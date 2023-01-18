package kz.divtech.odyssey.rotation.ui.profile.notification

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification

class NotificationCallBack(
    private val newNotificationList: List<Notification>,
    private val oldNotificationList: List<Notification>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNotificationList.size

    override fun getNewListSize(): Int = newNotificationList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newNotificationList[newItemPosition].id == oldNotificationList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newNotificationList[newItemPosition] == oldNotificationList[oldItemPosition]
}