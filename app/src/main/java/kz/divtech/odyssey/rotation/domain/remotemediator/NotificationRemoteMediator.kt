package kz.divtech.odyssey.rotation.domain.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification


@OptIn(ExperimentalPagingApi::class)
class NotificationRemoteMediator(val dao: Dao) : RemoteMediator<Int, Notification>() {

    private var pageIndex = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Notification>)
    : MediatorResult {
        pageIndex = getPageIndex(loadType) ?:
            return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val response = RetrofitClient.getApiService().getNotifications(pageIndex)
            val notifications = response.body()?.data!!
            when(loadType) {
                LoadType.REFRESH -> dao.refreshNotifications(notifications)
                else -> dao.insertNotifications(notifications)
            }
            MediatorResult.Success(notifications.size < state.config.pageSize)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {

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