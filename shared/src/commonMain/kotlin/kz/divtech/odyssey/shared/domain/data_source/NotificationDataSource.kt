package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification

interface NotificationDataSource {

    suspend fun getNotificationsPagingSource(): List<Notification>
    suspend fun getFirstThreeNotification(): Flow<List<Notification>>
    suspend fun insertNotification(notifications: List<Notification>)
    suspend fun refreshNotifications(notifications: List<Notification>)
    suspend fun deleteNoficiations()
}