package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticleReponse
import kz.divtech.odyssey.shared.domain.repository.ArticleRepository

class ArticleRepositoryImpl(private val httpClient: HttpClient,
                            private val dataStoreManager: DataStoreManager
): ArticleRepository {
    override suspend fun getArticleById(id: Int): Resource<FullArticleReponse> {
        return try {
            val result: FullArticleReponse = httpClient.get{
                url(HttpRoutes(dataStoreManager).getArticleById(id))
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

    override suspend fun markArticleAsRead(id: Int): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).markArticleAsRead(id))
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
}