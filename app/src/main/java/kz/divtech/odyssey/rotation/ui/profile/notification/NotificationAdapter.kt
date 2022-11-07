package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.databinding.ItemNotificationBinding

class NotificationAdapter(private val notificationList: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.notification = notificationList[position]
    }

    override fun getItemCount() : Int{
        return if(notificationList.size > Config.NOTIFICATION_LIMIT_SIZE){
            Config.NOTIFICATION_LIMIT_SIZE
        }else{
            notificationList.size
        }
    }

    class NotificationViewHolder(val binding : ItemNotificationBinding) : ViewHolder(binding.root)
}