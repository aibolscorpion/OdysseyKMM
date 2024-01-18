package kz.divtech.odyssey.shared.domain.data_source

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification

interface NotificationDataSource {
    fun getNotificationsPagingSource(): PagingSource<Int, Notification>
    suspend fun getFirstThreeNotification(): Flow<List<Notification>>
    suspend fun insertNotification(notifications: List<Notification>)
    suspend fun refreshNotifications(notifications: List<Notification>)
    suspend fun deleteNoficiations()
}