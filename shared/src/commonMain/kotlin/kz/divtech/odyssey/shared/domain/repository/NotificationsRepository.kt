package kz.divtech.odyssey.shared.domain.repository

import app.cash.paging.PagingData
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notifications

interface NotificationsRepository {
    suspend fun getNotificationsFirstPage(): Resource<Notifications>

    suspend fun markNotificationAsRead(notificationId: String): Resource<HttpResponse>

    fun getPagingNotification(): Flow<PagingData<Notification>>
    suspend fun getFirstThreeNotificationsFromBD(): Flow<List<Notification>>

    suspend fun deleteNoficiations()
}