package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.repository.TermsRepository

class TermsRepositoryImpl(private val httpClient: HttpClient,
                          private val dataStoreManager: DataStoreManager
): TermsRepository {

    override suspend fun getTermsOfAgreement(): Resource<String> {
        return try {
            val result: String = httpClient.get(HttpRoutes.GET_TERMS_OF_AGREEMENT).body()
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun updateUAConfirm(): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).updateUAConfirm())
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

}