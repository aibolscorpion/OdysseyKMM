package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import timber.log.Timber

class NotificationRepository(private val dao: Dao) {
    val notifications: Flow<List<Notification>> = dao.getNotifications()

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertNotifications(notificationList: List<Notification>){
        dao.insertNotifications(notificationList)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNotifications(){
        dao.deleteNotifications()
    }

    suspend fun getNotificationsFromServer(){
        try {
            val response = RetrofitClient.getApiService().getNotifications()
            insertNotifications(response.body()?.data!!)
        }catch (e: Exception){
            Timber.e("exception - $e")
        }
    }

}