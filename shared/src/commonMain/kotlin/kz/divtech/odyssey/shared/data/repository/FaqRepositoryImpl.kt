package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.FaqDataSource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq
import kz.divtech.odyssey.shared.domain.repository.FaqRepository

class FaqRepositoryImpl(private val httpClient: HttpClient,
                        private val dataSource: FaqDataSource): FaqRepository {
    override suspend fun getFaqList(): Resource<List<Faq>> {
        return try {
            val result: List<Faq> = httpClient.get{
                url(HttpRoutes.GET_FAQ_LIST )
            }.body()
            dataSource.refreshFaq(result)
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

    override suspend fun getFaqListFromDb(): List<Faq> {
        return dataSource.getFaq()
    }

    override suspend fun searchFaqFromDb(searchQuery: String): List<Faq> {
        return dataSource.searchFaq(searchQuery)
    }

    override suspend fun deleteFaq() {
        dataSource.deleteFaq()
    }
}