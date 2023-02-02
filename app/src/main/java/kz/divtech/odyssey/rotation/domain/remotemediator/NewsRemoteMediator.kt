package kz.divtech.odyssey.rotation.domain.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article

@ExperimentalPagingApi
class NewsRemoteMediator(private val dao: Dao) : RemoteMediator<Int, Article>() {
    private var pageIndex = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Article>): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
            return MediatorResult.Success(endOfPaginationReached = true)

        return try{
            val response = RetrofitClient.getApiService().getArticles(pageIndex)
            val news = response.body()?.data!!
            when(loadType){
                LoadType.REFRESH -> dao.refreshNews(news)
                else -> dao.insertNews(news)
            }
            MediatorResult.Success(news.size < state.config.pageSize)
        }catch (e: Exception){
            MediatorResult.Error(e)
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