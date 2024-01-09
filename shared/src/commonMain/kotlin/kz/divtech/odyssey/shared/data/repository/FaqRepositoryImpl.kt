package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
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