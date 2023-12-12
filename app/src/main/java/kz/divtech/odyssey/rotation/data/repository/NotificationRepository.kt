package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.common.Constants.NOTIFICATIONS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.data.remotemediator.NotificationRemoteMediator

class NotificationRepository(private val dao: Dao,private val apiService: ApiService) {
    val notifications: Flow<List<Notification>> = dao.observeThreeNotifications()

    @WorkerThread
    suspend fun refreshNotifications(notificationList: List<Notification>){
        dao.refreshNotifications(notificationList)
    }

    @WorkerThread
    suspend fun deleteNotifications(){
        dao.deleteNotifications()
    }

    suspend fun markNotificationAsRead(id: String){
        val map = mutableMapOf("notification_id" to id)
        apiService.markAsReadNotificationById(map)
    }

    suspend fun getNotificationFromFirstPage(){
        val response = apiService.getNotifications(1)
        if(response.isSuccess()){
            val notifications = response.asSuccess().value.data
            refreshNotifications(notifications)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingNotifications(): Flow<PagingData<Notification>>{
        return Pager(
            config = PagingConfig(pageSize = NOTIFICATIONS_PAGE_SIZE),
            remoteMediator = NotificationRemoteMediator(dao, apiService),
            pagingSourceFactory = { dao.getNotificationPagingSource() }
        ).flow
    }

}