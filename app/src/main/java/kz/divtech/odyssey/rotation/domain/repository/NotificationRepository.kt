package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATIONS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.remotemediator.NotificationRemoteMediator

class NotificationRepository(private val dao: Dao) {
    val notifications: Flow<List<Notification>> = dao.observeThreeNotifications()
    private var firstTime = true

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun refreshNotifications(notificationList: List<Notification>){
        dao.refreshNotifications(notificationList)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNotifications(){
        dao.deleteNotifications()
    }

    suspend fun markNotificationAsRead(id: String){
        val map = mutableMapOf("notification_id" to id)
        RetrofitClient.getApiService().markAsReadNotificationById(map)
    }

    suspend fun getNotificationsFromServer(){
        if(firstTime){
            val response = RetrofitClient.getApiService().getNotifications(1)
            if(response.isSuccess()){
                val notifications = response.asSuccess().value.data
                refreshNotifications(notifications)
                firstTime = false
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingNotifications(): Flow<PagingData<Notification>>{
        return Pager(
            config = PagingConfig(pageSize = NOTIFICATIONS_PAGE_SIZE),
            remoteMediator = NotificationRemoteMediator(dao),
            pagingSourceFactory = { dao.getNotificationPagingSource() }
        ).flow
    }

}