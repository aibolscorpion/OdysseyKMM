package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification

class NotificationPagingAdapter(val listener: NotificationListener) : PagingDataAdapter<Notification, NotificationViewHolder>(NotificationCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val viewHolder = NotificationViewHolder(ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        viewHolder.binding.listener = listener
        return viewHolder
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.notification = getItem(position)
    }
}

class NotificationViewHolder(val binding: ItemNotificationBinding)
    : RecyclerView.ViewHolder(binding.root) {

}

interface NotificationListener{
    fun onNotificationClicked(notification: Notification)
}