package kz.divtech.odyssey.shared.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
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
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.data.repository.pagingSource.NotificationPagingSource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.MarkNotification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notifications
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository

class NotificationsRepositoryImpl(private val httpClient: HttpClient): NotificationsRepository {
    override suspend fun getNotificationsFirstPage(): Resource<Notifications> {
        return try {
            val result: Notifications = httpClient.get {
                url(HttpRoutes.GET_NOTIFICATIONS)
                parameter("page", 1)
            }.body()
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Resource<HttpResponse> {
        return try {
            val notification = MarkNotification(notificationId)
            val result = httpClient.post{
                url(HttpRoutes.MARK_NOTIFICATION_AS_READ)
                contentType(ContentType.Application.Json)
                setBody(notification)
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override fun getPagingNotification(): Flow<PagingData<Notification>>{
        val pagingConfig = PagingConfig(pageSize = NOTIFICATION_PAGE_SIZE,
            initialLoadSize = NOTIFICATION_PAGE_SIZE * 3)
        return Pager(pagingConfig) {
            NotificationPagingSource(httpClient)
        }.flow
    }
}