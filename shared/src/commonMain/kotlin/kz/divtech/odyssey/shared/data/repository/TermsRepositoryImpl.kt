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
import kz.divtech.odyssey.shared.data.local.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.repository.TermsRepository

class TermsRepositoryImpl(private val httpClient: HttpClient,
                          private val dataStoreManager: DataStoreManager): TermsRepository {

    override suspend fun getTermsOfAgreement(): Resource<String> {
        return try {
            val result: String = httpClient.get(HttpRoutes.GET_TERMS_OF_AGREEMENT).body()
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

    override suspend fun updateUAConfirm(): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).updateUAConfirm())
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