package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundAppResponse
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundApplication
import kz.divtech.odyssey.shared.domain.repository.RefundRepository

class RefundRepositoryImpl(private val httpClient: HttpClient,
                           private val dataStoreManager: DataStoreManager
): RefundRepository {
    override suspend fun sendApplicationToRefund(application: RefundApplication): Resource<RefundAppResponse> {
        return try {
           val result: RefundAppResponse =  httpClient.post {
                contentType(ContentType.Application.Json)
                url(HttpRoutes(dataStoreManager).sendApplicationToRefund())
                setBody(application)
            }.body()
            Resource.Success(result)
        } catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun cancelRefund(id: Int): Resource<HttpResponse> {
        return try {
            val result = httpClient.post {
                url(HttpRoutes(dataStoreManager).cancelRefundById(id))
            }
            Resource.Success(result)
        } catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}