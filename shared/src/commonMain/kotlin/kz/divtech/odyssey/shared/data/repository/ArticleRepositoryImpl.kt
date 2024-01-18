package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.FullArticleDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticleResponse
import kz.divtech.odyssey.shared.domain.repository.ArticleRepository

class ArticleRepositoryImpl(private val httpClient: HttpClient,
                            private val dataStoreManager: DataStoreManager,
                            private val dataSource: FullArticleDataSource
): ArticleRepository {
    override suspend fun getArticleById(id: Int): Resource<FullArticleResponse> {
        return try {
            val result: FullArticleResponse = httpClient.get{
                url(HttpRoutes(dataStoreManager).getArticleById(id))
            }.body()
            dataSource.insertArticle(result.data)
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun markArticleAsRead(id: Int): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).markArticleAsRead(id))
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override fun getArticleFromDbById(id: Int): Flow<FullArticle?> {
        return dataSource.getArticleById(id)
    }

    override fun deleteFullArticles() {
        dataSource.deleteFullArticles()
    }
}