package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants.NEWS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.domain.remotemediator.NewsRemoteMediator

class NewsRepository(private val dao: Dao) {

    fun searchArticlesFromDB(searchQuery: String): Flow<List<Article>> = dao.searchArticle(searchQuery)

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNews(){
        dao.deleteNews()
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getPagingNews(): Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(pageSize = NEWS_PAGE_SIZE),
            remoteMediator = NewsRemoteMediator(dao),
            pagingSourceFactory = { dao.getNewsPagingSource() }
        ).flow
    }

}