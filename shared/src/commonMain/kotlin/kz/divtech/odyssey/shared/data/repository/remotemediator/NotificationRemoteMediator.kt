package kz.divtech.odyssey.shared.data.repository.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.NotificationDataSource
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notifications
import java.io.IOException

@ExperimentalPagingApi
class NotificationRemoteMediator(private val httpClient: HttpClient,
                                 private val dataSource: NotificationDataSource,
                                 private val dataStoreManager: DataStoreManager) : RemoteMediator<Int, Notification>() {
    private var pageIndex = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Notification>): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
        return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val httpResponse = httpClient.get{
                url(HttpRoutes(dataStoreManager).getNotifications())
                parameter("page", pageIndex)
            }
            val notifications: Notifications = httpResponse.body()
            when(loadType){
                LoadType.REFRESH -> dataSource.refreshNotifications(notifications.data)
                else -> dataSource.insertNotification(notifications.data)
            }
            MediatorResult.Success(notifications.data.size < state.config.pageSize)
        }catch (e: ClientRequestException) {
            return MediatorResult.Error(
                Exception(e.response.status.description),
            )
        }catch (e: ServerResponseException) {
            return MediatorResult.Error(
                Exception(e.response.status.description),
            )
        }catch (e: IOException) {
            return MediatorResult.Error(
                IOException("${e.message}"),
            )
        }catch (e: Exception){
            return MediatorResult.Error(
                Exception("${e.message}"),
            )
        }
    }

    private fun getPageIndex(loadType: LoadType): Int?{
        return when(loadType){
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> null
            LoadType.APPEND -> ++pageIndex
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }
}