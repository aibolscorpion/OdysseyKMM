package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.common.Constants.NEWS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.data.remotemediator.NewsRemoteMediator

class NewsRepository (private val dao: Dao, private val apiService: ApiService) {
    suspend fun searchArticlesFromDB(searchQuery: String) = dao.searchArticle(searchQuery)

    @WorkerThread
    suspend fun deleteNews(){
        dao.deleteNews()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingNews(): Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(pageSize = NEWS_PAGE_SIZE),
            remoteMediator = NewsRemoteMediator(dao, apiService),
            pagingSourceFactory = { dao.getNewsPagingSource() }
        ).flow
    }
}