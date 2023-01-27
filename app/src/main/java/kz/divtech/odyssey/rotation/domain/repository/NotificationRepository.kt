package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationRemoteMediator
import timber.log.Timber

const val NETWORK_PAGE_SIZE = 10

class NotificationRepository(private val dao: Dao) {

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNotifications(){
        dao.deleteNotifications()
    }

    suspend fun markNotificationAsRead(id: String){
        val map = mutableMapOf("notification_id" to id)
        try {
            RetrofitClient.getApiService().markAsReadNotificationById(map)
        }catch (e: Exception){
            Timber.e("exception - $e")
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getNotificationsFromServer(): Flow<PagingData<Notification>>{
            return Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = 20
                ),
                remoteMediator = NotificationRemoteMediator(dao),
                pagingSourceFactory = { dao.getNotificationPagingSource() }
            ).flow
    }

}