package kz.divtech.odyssey.shared.data.repository

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Constants.NOTIFICATION_PAGE_SIZE
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.data.repository.remotemediator.NotificationRemoteMediator
import kz.divtech.odyssey.shared.domain.data_source.NotificationDataSource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.MarkNotification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notifications
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository

class NotificationsRepositoryImpl(private val httpClient: HttpClient,
                                  private val dataStoreManager: DataStoreManager,
                                  private val dataSource: NotificationDataSource
): NotificationsRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingNotification(): Flow<PagingData<Notification>>{
        return Pager(
            config = PagingConfig(pageSize = NOTIFICATION_PAGE_SIZE),
            remoteMediator = NotificationRemoteMediator(httpClient, dataSource, dataStoreManager),
            pagingSourceFactory = { dataSource.getNotificationsPagingSource() }
        ).flow
    }
    override fun getFirstThreeNotificationsFromBD(): Flow<List<Notification>> {
        return dataSource.getFirstThreeNotification()
    }

    override fun deleteNoficiations() {
        dataSource.deleteNoficiations()
    }

    override suspend fun getNotificationsFirstPage(): Resource<Notifications> {
        return try {
            val result: Notifications = httpClient.get {
                url(HttpRoutes(dataStoreManager).getNotifications())
                parameter("page", 1)
            }.body()
            dataSource.refreshNotifications(result.data)
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Resource<HttpResponse> {
        return try {
            val notification = MarkNotification(notificationId)
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).markNotificationAsRead())
                contentType(ContentType.Application.Json)
                setBody(notification)
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }


}