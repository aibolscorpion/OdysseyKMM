package kz.divtech.odyssey.shared.domain.data_source

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification

interface NotificationDataSource {
    fun getNotificationsPagingSource(): PagingSource<Int, Notification>
     fun getFirstThreeNotification(): Flow<List<Notification>>
     fun insertNotification(notifications: List<Notification>)
     fun refreshNotifications(notifications: List<Notification>)
     fun deleteNoficiations()
}