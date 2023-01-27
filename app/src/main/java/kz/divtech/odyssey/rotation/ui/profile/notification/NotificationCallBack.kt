package kz.divtech.odyssey.rotation.ui.profile.notification

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification

class NotificationCallBack() : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

}