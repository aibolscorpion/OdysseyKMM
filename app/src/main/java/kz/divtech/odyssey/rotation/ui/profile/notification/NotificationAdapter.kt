package kz.divtech.odyssey.rotation.ui.profile.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.divtech.odyssey.rotation.databinding.ItemNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationListener

class NotificationAdapter(private val notificationListener: NotificationListener) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    private val oldNotificationList = mutableListOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = notificationListener
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.notification = oldNotificationList[position]
    }

    fun setNotificationList(newNotificationList: List<Notification>){
        val diffCallBack = NotificationCallBack(newNotificationList, oldNotificationList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        oldNotificationList.clear()
        oldNotificationList.addAll(newNotificationList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = oldNotificationList.size

    inner class NotificationViewHolder(val binding : ItemNotificationBinding) : ViewHolder(binding.root)
}
