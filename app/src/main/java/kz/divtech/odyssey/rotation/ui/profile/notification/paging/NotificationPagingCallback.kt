package kz.divtech.odyssey.rotation.ui.profile.notification.paging

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification

class NotificationPagingCallback() : DiffUtil.ItemCallback<Notification>() {

    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}