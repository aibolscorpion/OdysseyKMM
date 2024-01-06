package kz.divtech.odyssey.shared.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Constants
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.repository.pagingSource.NewsPagingSource
import kz.divtech.odyssey.shared.domain.data_source.NewsDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.shared.domain.repository.NewsRepository

class NewsRepositoryImpl(private val httpClient: HttpClient,
                         private val dataStoreManager: DataStoreManager,
                        private val dataSource: NewsDataSource
) : NewsRepository{
    override fun getPagingNews(): Flow<PagingData<Article>> {
        val pagingConfig = PagingConfig(pageSize = Constants.NEWS_PAGE_SIZE,
            initialLoadSize = Constants.NEWS_PAGE_SIZE * 3)
        return Pager(pagingConfig) {
            NewsPagingSource(httpClient, dataStoreManager)
        }.flow
    }

    override suspend fun getNewsFromDb(): List<Article> {
        return dataSource.getNews()
    }

    override suspend fun searchNewsFromDb(searchQuery: String): List<Article> {
        return dataSource.searchNews(searchQuery)
    }


    override suspend fun deleteNews() {
        dataSource.deleteNews()
    }
}