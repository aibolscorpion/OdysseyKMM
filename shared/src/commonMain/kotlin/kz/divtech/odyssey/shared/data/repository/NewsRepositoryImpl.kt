package kz.divtech.odyssey.shared.data.repository

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Constants
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.repository.remotemediator.NewsRemoteMediator
import kz.divtech.odyssey.shared.domain.data_source.NewsDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.shared.domain.repository.NewsRepository

class NewsRepositoryImpl(private val httpClient: HttpClient,
                         private val dataStoreManager: DataStoreManager,
                        private val dataSource: NewsDataSource
) : NewsRepository{
    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.NEWS_PAGE_SIZE),
            remoteMediator = NewsRemoteMediator(httpClient, dataSource, dataStoreManager),
            pagingSourceFactory = { dataSource.getNews() }
        ).flow
    }

    override suspend fun searchNewsFromDb(searchQuery: String): List<Article> {
        return dataSource.searchNews(searchQuery)
    }


    override suspend fun deleteNews() {
        dataSource.deleteNews()
    }
}